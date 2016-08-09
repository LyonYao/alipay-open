package com.ilyon.alipay.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.FileItem;
import com.alipay.api.request.AlipayOfflineMarketShopBatchqueryRequest;
import com.alipay.api.request.AlipayOfflineMarketShopCreateRequest;
import com.alipay.api.request.AlipayOfflineMaterialImageUploadRequest;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserUserinfoShareRequest;
import com.alipay.api.response.AlipayOfflineMarketShopBatchqueryResponse;
import com.alipay.api.response.AlipayOfflineMarketShopCreateResponse;
import com.alipay.api.response.AlipayOfflineMaterialImageUploadResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserUserinfoShareResponse;

public class AlipayAuthServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1268638344171205168L;
	private static final String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIgHnOn7LLILlKETd6BFRJ0GqgS2Y3mn1wMQmyh9zEyWlz5p1zrahRahbXAfCfSqshSNfqOmAQzSHRVjCqjsAw1jyqrXaPdKBmr90DIpIxmIyKXv4GGAkPyJ/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB";
	private static final String APP_PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAOdRPWYOdJILmNOAIfruYgJwwQoG9XJUqJX11SwpyZHLtdbWDRMmGmD8mBv0twCHTCbekoCrMABlPDkyRxlNRa5mWHrIupJMuGTM7rs5lH7Mn1bPbnm3ZEhOUFFripp0MRQsp+jpf75ALaPxfk7V7ax+JFG+OL90FVrnvEiR/hOnAgMBAAECgYEAyyMj6UkOg+bdSfd/X88SkRQV4klkKQhBmJfvob38vyWHRehqSQOwLLYGmp5YS4WFkajqPUaYe/BrZ8tIdubOJgNcEjzKmtvMYzq80piKCBqtLdK1CRoF52UEzq/jdhF3nUFPvg9t8f13kzdskSAe/iRHgO1yhHgAL3yGHv23ycECQQD1lQTdrztagUQbhcPTgJrgUrJRNebZEmEVkQgpxXHkQbUCAvTc+faFrRUFnWs2i5Uz8BjnRiNMeWKYT/BnTBOhAkEA8SFO0NP6/4uNUaEheLHNkFob7cU6I1PL1dETR+hnuaeqY2bbRt4xVBLpBGNpNoYURLZSpKR3cmjcA0z2AMFiRwJBAKQ8vd1YFJSpgJf7dIRdLLa694AyUSl5Di5zKQdugYWKn/gxa/rgvIqXv0hbB9mZrfXnK3B4SqEZSOPeCkO68CECQD9E6hVJSda7mz0L9mhg3vs9aY6d7X9PgXB9B34asPy5za9/UZHqwy64N4P7QaQY0kClRlIFBYJzmScUjofsNgkCQQDxhvAd/1C1g0dsp/d2DDsTNudAog1w29ywpvweRYJ3yhMoXyAY3qkXhCoCzihwoEOBtW07sepaboi2843oPWkb";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("-------------------------:"
				+ req.getParameter("auth_code"));
		String appId = req.getParameter("app_id");
		System.out.println("-------------------------1:"
				+ appId);
		PrintWriter writer = resp.getWriter();

		AlipayClient alipayClient = new DefaultAlipayClient(
				"https://openapi.alipaydev.com/gateway.do", appId,
				APP_PRIVATE_KEY, "json", "UTF-8", ALIPAY_PUBLIC_KEY);
		AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
		request.setCode(req.getParameter("auth_code"));
		request.setGrantType("authorization_code");
		try {
			AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient
					.execute(request);
			System.out.println(oauthTokenResponse.getCode());
			System.out.println(oauthTokenResponse.getAccessToken());

			AlipayUserUserinfoShareRequest request1 = new AlipayUserUserinfoShareRequest();
			String access_token = oauthTokenResponse.getAccessToken();
			AlipayUserUserinfoShareResponse userinfoShareResponse = alipayClient
					.execute(request1, access_token);
			writer.write(userinfoShareResponse.getBody());
			
			AlipayOfflineMaterialImageUploadRequest alipayRequest = new AlipayOfflineMaterialImageUploadRequest();
			alipayRequest.setImageType("jpg"); //图片类型，当前支持bmp,png,jpeg,jpg,gif 5个类型
			alipayRequest.setImageName("图片名称1");
			FileItem ImageContent = new FileItem("c:\\test1.jpg"); //图片文件路径
			alipayRequest.setImageContent(ImageContent);
			 
			AlipayOfflineMaterialImageUploadResponse alipayResponse1 = alipayClient.execute(alipayRequest);
			alipayRequest = new AlipayOfflineMaterialImageUploadRequest();
			alipayRequest.setImageType("jpg"); //图片类型，当前支持bmp,png,jpeg,jpg,gif 5个类型
			alipayRequest.setImageName("图片名称2");
			 ImageContent = new FileItem("c:\\test2.jpg"); //图片文件路径
			alipayRequest.setImageContent(ImageContent);
			 
			AlipayOfflineMaterialImageUploadResponse alipayResponse2 = alipayClient.execute(alipayRequest);
			alipayRequest = new AlipayOfflineMaterialImageUploadRequest();
			alipayRequest.setImageType("jpg"); //图片类型，当前支持bmp,png,jpeg,jpg,gif 5个类型
			alipayRequest.setImageName("图片名称3");
			 ImageContent = new FileItem("c:\\test3.jpg"); //图片文件路径
			alipayRequest.setImageContent(ImageContent);
			 
			AlipayOfflineMaterialImageUploadResponse alipayResponse3 = alipayClient.execute(alipayRequest);
			alipayRequest = new AlipayOfflineMaterialImageUploadRequest();
			alipayRequest.setImageType("jpg"); //图片类型，当前支持bmp,png,jpeg,jpg,gif 5个类型
			alipayRequest.setImageName("图片名称4");
			 ImageContent = new FileItem("c:\\test3.jpg"); //图片文件路径
			alipayRequest.setImageContent(ImageContent);
			 
			AlipayOfflineMaterialImageUploadResponse alipayResponse4 = alipayClient.execute(alipayRequest);
			alipayRequest = new AlipayOfflineMaterialImageUploadRequest();
			alipayRequest.setImageType("jpg"); //图片类型，当前支持bmp,png,jpeg,jpg,gif 5个类型
			alipayRequest.setImageName("图片名称5");
			 ImageContent = new FileItem("c:\\test3.jpg"); //图片文件路径
			alipayRequest.setImageContent(ImageContent);
			 
			AlipayOfflineMaterialImageUploadResponse alipayResponse5 = alipayClient.execute(alipayRequest);
			AlipayOfflineMarketShopBatchqueryRequest request4 = new AlipayOfflineMarketShopBatchqueryRequest();
			request4.setBizContent("{" +
			"    \"page_no\":\"1\"" +
			"  }");
			AlipayOfflineMarketShopBatchqueryResponse response4 = alipayClient.execute(request4);
			System.out.println(response4.getBody());
			AlipayOfflineMarketShopCreateRequest request3 = new AlipayOfflineMarketShopCreateRequest();
			request3.setBizContent("{" +
			"    \"store_id\":\"hz003"+(int)Math.random()*10+"\"," +
			"    \"category_id\":\"2015050700000018\"," +
			"    \"brand_name\":\"肯德基+\"," +
			"    \"brand_logo\":\""+alipayResponse1.getImageId()+"\"," +
			"    \"main_shop_name\":\"海底捞+\"," +
			"    \"branch_shop_name\":\"万塘路店+\"," +
			"    \"province_code\":\"110000\"," +
			"    \"city_code\":\"140500\"," +
			"    \"district_code\":\"140521\"," +
			"    \"address\":\"万塘路18号黄龙时代广场\"," +
			"    \"longitude\":114.266418," +
			"    \"latitude\":\"30.548828\"," +
			"    \"contact_number\":\"13612344321,021-12336754\"," +
			"    \"notify_mobile\":\"13867498729\"," +
			"    \"main_image\":\""+alipayResponse2.getImageId()+"\"," +
			"    \"audit_images\":\""+alipayResponse3.getImageId()+","+alipayResponse4.getImageId()+","+alipayResponse5.getImageId()+"\"," +
			"    \"business_time\":\"09:00-11:00,13:00-15:00\"," +
			"    \"wifi\":\"T\"," +
			"    \"parking\":\"F\"," +
			"    \"value_added\":\"免费茶水、免费糖果\"," +
			"    \"avg_price\":\"35.83\"," +
			"    \"isv_uid\":\""+oauthTokenResponse.getAlipayUserId()+"\"," +
			"    \"licence\":\"1T8Pp00AT7eo9NoAJkMR3AAAACMAAQEC\"," +
			"    \"licence_code\":\"H001232\"," +
			"    \"licence_name\":\"来伊份上海分公司\"," +
			"    \"business_certificate\":\"1T8Pp00AT7eo9NoAJkMR3AAAACMAAQEC\"," +
			"    \"business_certificate_expires\":\"2020-03-20\"," +
			"    \"auth_letter\":\"1T8Pp00AT7eo9NoAJkMR3AAAACMAAQEC\"," +
			"    \"is_operating_online\":\"T\"," +
			"    \"online_url\":\"http://www.xxx1.com/shop/21831830\"," +
			"    \"operate_notify_url\":\"http://abc.com\"," +
			"    \"implement_id\":\"HX002,HXX002\"," +
			"    \"no_smoking\":\"T\"," +
			"    \"box\":\"T\"," +
			"    \"request_id\":\""+System.currentTimeMillis()+"\"," +
			"    \"other_authorization\":\"1T8Pp00AT7eo9NoAJkMR3AAAACMAAQEC,1T8Pp00AT7eo9NoAJkMR3AAAACMAAQEC\"," +
			"    \"licence_expires\":\"2020-10-20\"," +
			"    \"op_role\":\"ISV\"," +
			"    \"biz_version\":\"2.0\"" +
			"  }");
			AlipayOfflineMarketShopCreateResponse response = alipayClient.execute(request3);
			System.out.println(response.getBody());
		} catch (AlipayApiException e) {
			// 处理异常
			e.printStackTrace();
		}
		
	
		writer.write("Hello alipay!");
		writer.close();

	}

}
