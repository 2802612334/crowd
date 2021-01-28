package com.atguigu.crowd.handler;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.atguigu.crowd.api.MySQLRemoteFeignService;
import com.atguigu.crowd.config.AliPayProperties;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.OrderProjectVO;
import com.atguigu.crowd.entity.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/pay")
public class PayConsumerHandler {

    @Autowired
    private MySQLRemoteFeignService mySQLRemoteFeignService;

    @Autowired
    private AliPayProperties aliPayProperties;

    @ResponseBody
    @RequestMapping("/generate/order")
    public String generateOrder(HttpSession session, OrderVO orderVO){
        // 1.session中取出orderProjectVO
        OrderProjectVO orderProjectVO = (OrderProjectVO)session.getAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT);
        // 2.将该对象设置到OrderVO
        orderVO.setOrderProjectVO(orderProjectVO);
        // 3.生成订单号
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String user = UUID.randomUUID().toString().replace("-","").toUpperCase();
        String orderNum = time + user;
        orderVO.setOrderNum(orderNum);
        // 4.计算订单总金额
        double orderAmmount = (double)(orderProjectVO.getReturnNum() * orderProjectVO.getSupportPrice() + orderProjectVO.getFreight());
        orderVO.setOrderAmount(orderAmmount);
        // 5.调用封装的支付方法为支付宝接口发送请求
        try {
            return sendRequestToAliPay(orderVO.getOrderNum(),orderVO.getOrderAmount(),orderProjectVO.getProjectName(),orderProjectVO.getReturnContent());
        } catch (AlipayApiException e) {
            return CrowdConstant.MESSAGE_PAY_FEILED;
        }
    }

    /**
     * @return 返回支付宝生成的订单号，以及商户的订单号和支付金额
     * */
    @ResponseBody
    @RequestMapping("/return")
    public String returnUrlMethod(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {
        // 获取支付宝 GET 过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        boolean signVerified = AlipaySignature.rsaCheckV1(
                params, aliPayProperties.getAlipayPublicKey(), aliPayProperties.getCharset(), aliPayProperties.getSignType()); //调用 SDK 验证签名
        if(signVerified) {
            // 商户订单号
            String orderNum = new
                    String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            // 支付宝交易号
            String payOrderNum = new
                    String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            // 付款金额
            String orderAmount = new
                    String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
            return "商品订单号：" + orderNum + "\n" + "支付宝交易号：" + payOrderNum + "\n" + "付款金额：" + orderAmount;
        }else {
            return "支付失败";
        }
    }

    /**
     * @return 返回支付是否成功，支付之后的需要的操作
     * */
    @ResponseBody
    @RequestMapping("/notifyUrl")
    public String notifyUrlMethod(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {
        // 获取支付宝 GET 过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        boolean signVerified = AlipaySignature.rsaCheckV1(
                params, aliPayProperties.getAlipayPublicKey(), aliPayProperties.getCharset(), aliPayProperties.getSignType()); //调用 SDK 验证签名
        if(signVerified){
            return "支付成功";
        }else {
            return "支付失败";
        }
    }
    /**
     * @param outTradeNo 商户订单号
     * @param totalAmount 订单金额
     * @param subject 订单标题
     * @param body 商品描述
     * @return 返回页面上显示支付宝登录界面
     * */
    private String sendRequestToAliPay(String outTradeNo, Double totalAmount, String subject, String body) throws AlipayApiException {
        //获得初始化的 AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                aliPayProperties.getGatewayUrl(),
                aliPayProperties.getAppId(),
                aliPayProperties.getMerchantPrivateKey(),
                "json",
                aliPayProperties.getCharset(),
                aliPayProperties.getAlipayPublicKey(),
                aliPayProperties.getSignType());
        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(aliPayProperties.getReturnUrl());
        alipayRequest.setNotifyUrl(aliPayProperties.getNotifyUrl());
        alipayRequest.setBizContent("{\"out_trade_no\":\""+ outTradeNo +"\"," + "\"total_amount\":\""+ totalAmount +"\"," + "\"subject\":\""+ subject +"\"," + "\"body\":\""+ body +"\"," + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        // 若 想 给 BizContent 增 加 其 他 可 选 请 求 参 数 ， 以 增 加 自 定 义 超 时 时 间 参 数timeout_express 来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        // + "\"total_amount\":\""+ total_amount +"\","
        // + "\"subject\":\""+ subject +"\","
        // + "\"body\":\""+ body +"\","
        // + "\"timeout_express\":\"10m\","
        // + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的 API 文档-alipay.trade.page.pay-请求参数】章节
        //请求
        return alipayClient.pageExecute(alipayRequest).getBody();
    }
}
