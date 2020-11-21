package com.example.school_bus.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.school_bus.R;
import com.mob.MobSDK;
import com.mob.secverify.datatype.LandUiSettings;
import com.mob.secverify.datatype.UiSettings;
import com.mob.secverify.ui.AgreementPage;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.List;

public class CustomizeUtils {
	private static String url;

	public static UiSettings customizeUi(){
		return new UiSettings.Builder()
				.setAgreementUncheckHintType(0)
				/** 标题栏 */
				// 标题栏背景色资源ID
				.setNavColorId(R.color.sec_verify_demo_text_color_blue)
				//标题栏是否透明
				.setNavTransparent(true)
				//标题栏是否隐藏
				.setNavHidden(false)
				//设置背景图片
//				.setBackgroundImgId(R.drawable.sec_verify_background_demo)
				//设置背景是否点击关闭页面
//				.setBackgroundClickClose(false)
				// 标题栏标题文字资源ID
//				.setNavTextId(R.string.sec_verify_demo_verify)
				// 标题栏文字颜色资源ID
//				.setNavTextColorId(R.color.sec_verify_demo_text_color_common_black)
				// 标题栏左侧关闭按钮图片资源ID
				.setNavCloseImgId(R.drawable.sec_verify_demo_back)
				//标题栏返回按钮是否隐藏
				.setNavCloseImgHidden(false)
				/** Logo */
				// Logo图片资源ID，默认使用应用图标
				.setLogoImgId(R.drawable.sec_verify_demo_logo)
				//logo是否隐藏
				.setLogoHidden(false)
//				//logo宽度
				.setLogoWidth(R.dimen.sec_verify_demo_common_80)
				//logo高度
				.setLogoHeight(R.dimen.sec_verify_demo_common_80)
				//logo x轴偏移量
//				.setLogoOffsetX(R.dimen.sec_verify_demo_common_30)
				//logo y轴偏移量
//				.setLogoOffsetY(R.dimen.sec_verify_demo_common_300)
				/** 手机号 */
				// 脱敏手机号字体颜色资源ID
				.setNumberColorId(R.color.sec_verify_demo_text_color_common_black)
				// 脱敏手机号字体大小资源ID
				.setNumberSizeId(R.dimen.sec_verify_demo_text_size_m)
				//脱敏手机号 x轴偏移量
//				.setNumberOffsetX(R.dimen.sec_verify_demo_common_150)
				//脱敏手机号 y轴偏移量
//				.setNumberOffsetY(R.dimen.sec_verify_demo_common_300)
				/** 切换帐号 */
				// 切换账号字体颜色资源ID
				.setSwitchAccColorId(R.color.sec_verify_demo_text_color_blue)
				//切换账号 字体大小
				.setSwitchAccTextSize(R.dimen.sec_verify_demo_text_size_s)
				// 切换账号是否显示，默认显示
				.setSwitchAccHidden(true)
				.setSwitchAccOffsetBottomY(50)
				//切换账号 x轴偏移量
//				.setSwitchAccOffsetX(R.dimen.sec_verify_demo_common_180)
				//切换账号 y轴偏移量
//				.setSwitchAccOffsetY(R.dimen.sec_verify_demo_switch_acc_offset_y)

				/** 登录按钮 */
				// 登录按钮背景图资源ID，建议使用shape
				.setLoginBtnImgId(R.drawable.sec_verify_demo_shape_rectangle)
				// 登录按钮文字资源ID
				.setLoginBtnTextId(R.string.sec_verify_demo_one_key_login)
				// 登录按钮字体颜色资源ID
				.setLoginBtnTextColorId(R.color.sec_verify_demo_text_color_common_white)
				//登录按钮字体大小
				.setLoginBtnTextSize(R.dimen.sec_verify_demo_text_size_s)
				//登录按钮 width
				.setLoginBtnWidth(250)
				//登录按钮 height
				.setLoginBtnHeight(45)
				//登录按钮 x轴偏移
//				.setLoginBtnOffsetX(R.dimen.sec_verify_demo_common_20)
				//登录按钮 y轴偏移
//				.setLoginBtnOffsetY(R.dimen.sec_verify_demo_common_400)
				/** 隐私协议 */
				//是否隐藏复选框(设置此属性true时setCheckboxDefaultState不会生效)
				.setCheckboxHidden(false)
				// 隐私协议复选框背景图资源ID，建议使用selector
				.setCheckboxImgId(R.drawable.sec_verify_demo_customized_checkbox_selector)
				// 隐私协议复选框默认状态，默认为“选中”
				.setCheckboxDefaultState(true)
				// 隐私协议字体颜色资源ID（自定义隐私协议的字体颜色也受该值影响）
//				.setAgreementColorId(R.color.sec_verify_demo_main_color)
				// 自定义隐私协议一文字资源ID
				.setAgreementHidden(false)
				.setCusAgreementNameId1(R.string.sec_verify_demo_mob_service)
//				// 自定义隐私协议一URL
				.setCusAgreementUrl1("http://www.mob.com/policy/zh")
////				自定义隐私协议一颜色
//				.setCusAgreementColor1(R.color.sec_verify_demo_main_color)
//				// 自定义隐私协议二文字资源ID
//				.setCusAgreementNameId2(R.string.sec_verify_demo_customize_agreement_name_2)
//				// 自定义隐私协议二URL
//				.setCusAgreementUrl2("https://www.jianshu.com")
////				自定义隐私协议二颜色
//				.setCusAgreementColor2(R.color.sec_verify_demo_main_color)
//				.setCusAgreementNameId3("自有服务策略")
//				.setCusAgreementUrl3("http://www.baidu.com")
//				.setCusAgreementColor3(R.color.sec_verify_demo_main_color)
//				.setAgreementTextAnd3("&")
				//隐私协议是否左对齐，默认居中
				.setAgreementGravityLeft(true)
				//隐私协议其他文字颜色
//				.setAgreementBaseTextColorId(R.color.sec_verify_demo_text_color_common_black)
				//隐私协议 x轴偏移量，默认30dp
				.setAgreementOffsetX(R.dimen.sec_verify_demo_common_50)
				//隐私协议 rightMargin右偏移量，默认30dp
				.setAgreementOffsetRightX(R.dimen.sec_verify_demo_common_50)
				//隐私协议 y轴偏移量
//				.setAgreementOffsetY(R.dimen.sec_verify_demo_common_50)
				//隐私协议 底部y轴偏移量
//				.setAgreementOffsetBottomY(R.dimen.sec_verify_demo_agreement_offset_bottom_y)
				/** slogan */
				//slogan文字大小
				.setSloganTextSize(R.dimen.sec_verify_demo_text_size_xs)
				.setSloganOffsetY(180)
				//slogan文字颜色
				.setSloganTextColor(R.color.sec_verify_demo_text_color_common_gray)
				//slogan x轴偏移量
//				.setSloganOffsetX(R.dimen.sec_verify_demo_common_200)
				//slogan y轴偏移量
//				.setSloganOffsetY(R.dimen.sec_verify_demo_common_180)
				//slogan 底部y轴偏移量(设置此属性时，setSloganOffsetY不生效)
//				.setSloganOffsetBottomY(R.dimen.sec_verify_demo_common_20)
				//设置状态栏为透明状态栏，5.0以上生效
				.setImmersiveTheme(true)
				//设置状态栏文字颜色为黑色，只在6.0以上生效
				.setImmersiveStatusTextColorBlack(true)
				//使用平移动画
//				.setTranslateAnim(true)
				.setStartActivityTransitionAnim(R.anim.translate_in, R.anim.translate_out)
				.setFinishActivityTransitionAnim(R.anim.translate_in, R.anim.translate_out)
				//设置隐私协议文字起始
//				.setAgreementTextStart(R.string.sec_verify_demo_agreement_text_start)
//				//设置隐私协议文字连接
//				.setAgreementTextAnd1(R.string.sec_verify_demo_agreement_text_and1)
//				//设置隐私协议文字连接
//				.setAgreementTextAnd2(R.string.sec_verify_demo_agreement_text_and2)
//				//设置隐私协议文字结束
//				.setAgreementTextEnd(R.string.sec_verify_demo_agreement_text_end)
//				//设置移动隐私协议文字
//				.setAgreementCmccText(R.string.sec_verify_demo_agreement_text_cmcc)
//				//设置联通隐私协议文字
//				.setAgreementCuccText(R.string.sec_verify_demo_agreement_text_cucc)
//				//设置电信隐私协议文字
//				.setAgreementCtccText(R.string.sec_verify_demo_agreement_text_ctcc)
//				//设置自定义隐私协议SpannableString
//				.setAgreementText(buildSpanString())
//				//设置自定义隐私协议未勾选时提示资源文件
//				.setAgreementUncheckHintText(R.string.ct_account_brand_text)
				//设置自定义隐私协议未勾选时提示
				.setAgreementUncheckHintText("请阅读并勾选隐私协议")
				//设置隐私协议文字起始
				.setAgreementTextStart("同意")
				//设置隐私协议文字结束
				.setAgreementTextEnd("并授权秒验Demo获取本机号码")
				//设置移动隐私协议文字
				.setAgreementCmccText("《移动统一认证服务条款》")
				//设置联通隐私协议文字
				.setAgreementCuccText("《联通统一认证服务条款》")
				//设置电信隐私协议文字
				.setAgreementCtccText("《电信统一认证服务条款》")
				//设置隐私协议文字颜色
				.setAgreementColorId(R.color.sec_verify_demo_main_color)
				//设置隐私协议左间距
				.setAgreementOffsetX(80)
				//设置隐私协议右间距
				.setAgreementOffsetRightX(80)
				.build();
	}


	public static LandUiSettings customizeLandUi() {
		return new LandUiSettings.Builder()
				//透明状态栏相关设置
				.setImmersiveTheme(true)
				.setImmersiveStatusTextColorBlack(true)
				//导航栏关闭按钮图片设置
				.setNavCloseImgId(R.drawable.sec_verify_demo_back)
				//Logo相关设置
				.setLogoImgId(R.drawable.sec_verify_demo_logo_small)
				.setLogoWidth(45)
				.setLogoHeight(45)
				.setLogoOffsetY(10)
				//脱敏手机号相关设置
				.setNumberOffsetY(60)
				.setNumberSizeId(16)
				//Slogan相关设置
				.setSloganOffsetY(90)
				//登录按钮相关设置
				.setLoginBtnOffsetY(120)
				.setLoginBtnImgId(R.drawable.sec_verify_demo_shape_rectangle)
				.setLoginBtnTextId("一键登录")
				//切换登录相关设置
				.setSwitchAccHidden(true)
				//隐私协议复选框设置
				.setCheckboxHidden(false)
				.setCheckboxDefaultState(true)
				.setCheckboxImgId(R.drawable.sec_verify_demo_customized_checkbox_selector)
				//隐私协议设置
				.setAgreementOffsetBottomY(20)
				.setAgreementColorId(R.color.sec_verify_demo_main_color)
				.setCusAgreementNameId1(R.string.sec_verify_demo_mob_service)
				.setCusAgreementUrl1("http://www.mob.com/policy/zh")
				.setAgreementTextStart("同意")
				.setAgreementTextEnd("并授权秒验Demo获取本机号码")
				.setAgreementCmccText("《移动统一认证服务条款》")
				.setAgreementCuccText("《联通统一认证服务条款》")
				.setAgreementCtccText("《电信统一认证服务条款》")
				//动画设置
				.setStartActivityTransitionAnim(R.anim.translate_in, R.anim.translate_out)
				.setFinishActivityTransitionAnim(R.anim.translate_in, R.anim.translate_out)
				.build();
	}

	public static UiSettings customizeUi4(){
		return new UiSettings.Builder()
				//导航栏相关设置
				.setNavCloseImgOffsetRightX(R.dimen.sec_verify_demo_common_5)
				.setNavColorId(R.color.sec_verify_demo_main_color)
				//Logo相关设置
				.setLogoImgId(R.drawable.sec_verify_demo_logo_small)
				.setLogoHeight(30)
				.setLogoWidth(30)
				.setLogoOffsetX(110)
				.setLogoOffsetY(1)
				//脱敏手机号相关设置
				.setNumberOffsetY(45)
				//slogan相关设置
				.setSloganOffsetY(80)
				//登录按钮相关设置
				.setLoginBtnOffsetY(120)
				.setLoginBtnWidth(250)
				.setLoginBtnImgId(R.drawable.sec_verify_demo_shape_rectangle)
				.setLoginBtnTextId("一键登录")
				.setLogoHidden(true)
				//切换登录相关设置
				.setSwitchAccHidden(true)
				//隐私协议复选框相关设置
				.setCheckboxHidden(false)
				.setCheckboxDefaultState(true)
				.setCheckboxImgId(R.drawable.sec_verify_demo_customized_checkbox_selector)
				//隐私协议相关设置
				.setAgreementOffsetY(180)
				.setAgreementOffsetX(60)
				.setAgreementOffsetRightX(60)
				.setAgreementColorId(R.color.sec_verify_demo_main_color)
				.setAgreementTextStart("同意")
				.setAgreementTextEnd("并授权秒验Demo获取本机号码")
				.setAgreementCmccText("《移动统一认证服务条款》")
				.setAgreementCuccText("《联通统一认证服务条款》")
				.setAgreementCtccText("《电信统一认证服务条款》")
//				.setStartActivityTransitionAnim(R.anim.fade_in,R.anim.fade_out)
//				.setFinishActivityTransitionAnim(R.anim.fade_in,R.anim.fade_out)
				.setCusAgreementNameId1(R.string.sec_verify_demo_mob_service)
				.setCusAgreementUrl1("http://www.mob.com/policy/zh")
				//动画相关设置
				.setBottomTranslateAnim(true)
				//dialog相关设置
				//蒙版背景点击关闭
				.setDialogMaskBackgroundClickClose(true)
				//dialog背景
				.setBackgroundImgId(R.drawable.sec_verify_background_demo_dialog)
				.setDialogTheme(true)
				.setDialogAlignBottom(false)
				.setDialogWidth(350)
				.setDialogHeight(300)
				.setDialogOffsetX(60)
				.setDialogOffsetY(R.dimen.sec_verify_demo_common_30)
				.build();
	}

	public static LandUiSettings customizeLandUi4(){
		return new LandUiSettings.Builder()
				//导航栏相关设置
				.setNavCloseImgOffsetRightX(R.dimen.sec_verify_demo_common_5)
				.setNavColorId(R.color.sec_verify_demo_main_color)
				//Logo相关设置
				.setLogoImgId(R.drawable.sec_verify_demo_logo_small)
				.setLogoHeight(30)
				.setLogoWidth(30)
				.setLogoOffsetY(1)
				.setLogoOffsetX(110) // 屏幕宽度 - 图标宽度
				.setLogoHidden(true)
				//脱敏手机号相关设置
				.setNumberOffsetY(45)
				.setNumberOffsetX(95)
				//slogan相关设置
				.setSloganOffsetY(75)
				//登录按钮相关设置
				.setLoginBtnOffsetY(100)
				.setLoginBtnWidth(200)
				.setLoginBtnImgId(R.drawable.sec_verify_demo_shape_rectangle)
				.setLoginBtnTextId("一键登录")
				//切换登录相关设置
				.setSwitchAccHidden(true)
				//隐私协议复选框相关设置
				.setCheckboxHidden(false)
				.setCheckboxDefaultState(true)
				.setCheckboxImgId(R.drawable.sec_verify_demo_customized_checkbox_selector)
				//隐私协议相关设置
				.setAgreementOffsetY(150)
				.setAgreementOffsetX(60)
				.setAgreementOffsetRightX(60)
				.setAgreementColorId(R.color.sec_verify_demo_main_color)
				.setCusAgreementNameId1(R.string.sec_verify_demo_mob_service)
				.setCusAgreementUrl1("http://www.mob.com/policy/zh")
				.setAgreementTextStart("同意")
				.setAgreementTextEnd("并授权秒验Demo获取本机号码")
				.setAgreementCmccText("《移动统一认证服务条款》")
				.setAgreementCuccText("《联通统一认证服务条款》")
				.setAgreementCtccText("《电信统一认证服务条款》")
//				.setStartActivityTransitionAnim(R.anim.fade_in,R.anim.fade_out)
//				.setFinishActivityTransitionAnim(R.anim.fade_in,R.anim.fade_out)
				//动画相关设置
				.setBottomTranslateAnim(true)
				//dialog相关设置
				//蒙版背景点击关闭
				.setDialogMaskBackgroundClickClose(true)
				//dialog背景
				.setBackgroundImgId(R.drawable.sec_verify_background_demo_dialog)
				.setDialogTheme(true)
				.setDialogAlignBottom(false)
				.setDialogWidth(300)
				.setDialogHeight(250)
				.setDialogOffsetX(60)
				.setDialogOffsetY(R.dimen.sec_verify_demo_common_30)
				.build();
	}

	/**
	 * 设置自定义控件
	 * @param context
	 * @return
	 */
	public static List<View> buildCustomView(Context context){
		TextView view = new TextView(context);
		view.setId(R.id.customized_view_id);
		view.setText("其它方式登录");
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.bottomMargin = ResHelper.dipToPx(context,95);
		view.setLayoutParams(params);


		TextView point = new TextView(context);
		point.setId(R.id.customized_btn_id_2);
		RelativeLayout.LayoutParams pointParams1 = new RelativeLayout.LayoutParams(1, 1);
		pointParams1.addRule(RelativeLayout.CENTER_HORIZONTAL);
		pointParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		pointParams1.bottomMargin = ResHelper.dipToPx(context,50);
		point.setLayoutParams(pointParams1);

		ImageView wechat = new ImageView(context);
		wechat.setId(R.id.customized_btn_id_1);
		wechat.setImageDrawable(context.getResources().getDrawable(R.drawable.sec_verify_demo_wechat));
		RelativeLayout.LayoutParams wechatParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		wechatParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		wechatParams1.addRule(RelativeLayout.LEFT_OF, R.id.customized_btn_id_2);
		wechatParams1.rightMargin = ResHelper.dipToPx(context,20);
		wechatParams1.bottomMargin = ResHelper.dipToPx(context,50);
		wechat.setLayoutParams(wechatParams1);


		ImageView user = new ImageView(context);
		user.setId(R.id.customized_btn_id_3);
		user.setImageDrawable(context.getResources().getDrawable(R.drawable.sec_verify_demo_user));
		RelativeLayout.LayoutParams userParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		userParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		userParams1.addRule(RelativeLayout.RIGHT_OF, R.id.customized_btn_id_2);
		userParams1.leftMargin = ResHelper.dipToPx(context,20);
		userParams1.bottomMargin = ResHelper.dipToPx(context,50);
		user.setLayoutParams(userParams1);

		List<View> views = new ArrayList<View>();
		views.add(view);
		views.add(wechat);
		views.add(point);
		views.add(user);
		return views;
	}


	/**
	 * 设置自定义控件
	 * @param context
	 * @return
	 */
	public static List<View> buildCustomView4(Context context){
		Drawable logo = context.getResources().getDrawable(R.drawable.sec_verify_demo_logo_small);
		logo.setBounds(0, 0, ResHelper.dipToPx(context, 30), ResHelper.dipToPx(context, 30));
		TextView view = new TextView(context);
		view.setId(R.id.customized_view_id);
		view.setText(R.string.app_name);
		view.setTextSize(20);
		view.setTextColor(context.getResources().getColor(R.color.sec_verify_demo_text_color_common_black));
		view.setTypeface(Typeface.DEFAULT_BOLD);
		view.setGravity(Gravity.CENTER);
		view.setCompoundDrawables(logo, null, null, null);
		view.setCompoundDrawablePadding(ResHelper.dipToPx(context, 15));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//		params.addRule(RelativeLayout.RIGHT_OF,R.id.sec_verify_page_one_key_login_logo_iv);
//		params.addRule(RelativeLayout.ALIGN_TOP,R.id.sec_verify_page_one_key_login_logo_iv);
//		params.leftMargin = ResHelper.dipToPx(context, 15);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.topMargin = ResHelper.dipToPx(context, 5);
		view.setLayoutParams(params);



		List<View> views = new ArrayList<View>();
		views.add(view);
		return views;
	}

	/**
	 * 设置自定义的隐私栏文本以及跳转
	 * @return
	 */
	private static SpannableString buildSpanString() {
		String operatorText = "";
		if (OperatorUtils.getCellularOperatorType() == 1){
			operatorText = "《中国移动认证服务条款》";
			url = "https://wap.cmpassport.com/resources/html/contract.html";
		} else if (OperatorUtils.getCellularOperatorType() == 2){
			operatorText = "《中国联通认证服务条款》";
			url = "https://ms.zzx9.cn/html/oauth/protocol2.html";
		} else  if (OperatorUtils.getCellularOperatorType() == 3){
			operatorText = "《中国电信认证服务条款》";
			url = "https://e.189.cn/sdk/agreement/content.do?type=main&appKey=&hidetop=true&returnUrl=";
		}
		String ageementText = "同意"+operatorText+"及《自有隐私协议》和" +
				"《自有服务策略》、《其他隐私协议》并授权秒验使用本机号码登录";
		String cusPrivacy1 = "《自有隐私协议》";
		String cusPrivacy2 = "《自有服务策略》";
		String cusPrivacy3 = "《其他隐私协议》";
		int baseColor = MobSDK.getContext().getResources().getColor(R.color.sec_verify_demo_text_color_common_black);
		int privacyColor = Color.parseColor("#FFFE7A4E");
		int cusPrivacyColor1 = Color.parseColor("#FF4E96FF");
		int cusPrivacyColor2 = Color.parseColor("#FF4E96FF");
		int cusPrivacyColor3 = Color.parseColor("#FFFE7A4E");
		SpannableString spanStr = new SpannableString(ageementText);
		int privacyIndex = ageementText.indexOf(operatorText);
		spanStr.setSpan(new ForegroundColorSpan(baseColor)
				, 0, ageementText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		//设置文字的单击事件
		spanStr.setSpan(new ClickableSpan() {
			@Override
			public void updateDrawState(TextPaint ds) {
				ds.setUnderlineText(false);
			}

			@Override
			public void onClick(View widget) {
				gotoAgreementPage(url,"");
			}
		}, privacyIndex, privacyIndex + operatorText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		//设置文字的前景色
		spanStr.setSpan(new ForegroundColorSpan(privacyColor), privacyIndex, privacyIndex + operatorText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		if (!TextUtils.isEmpty(cusPrivacy1)) {
			int privacy1Index = ageementText.indexOf(cusPrivacy1);
			//设置文字的单击事件
			spanStr.setSpan(new ClickableSpan() {
				@Override
				public void updateDrawState(TextPaint ds) {
					ds.setUnderlineText(false);
				}

				@Override
				public void onClick(View widget) {
					gotoAgreementPage("https://www.mob.com", null);
//					if (wrapper != null && wrapper.cusAgreement1Clicked != null){
//						wrapper.cusAgreement1Clicked.handle();
//					}
				}
			}, privacy1Index, privacy1Index + "《自有隐私协议》".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置文字的前景色
			spanStr.setSpan(new ForegroundColorSpan(cusPrivacyColor1), privacy1Index, privacy1Index + cusPrivacy1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		if (!TextUtils.isEmpty(cusPrivacy2)) {
			int privacy2Index = ageementText.lastIndexOf(cusPrivacy2);
			//设置文字的单击事件
			spanStr.setSpan(new ClickableSpan() {
				@Override
				public void updateDrawState(TextPaint ds) {
					ds.setUnderlineText(false);
				}

				@Override
				public void onClick(View widget) {
					gotoAgreementPage("https://www.baidu.com", null);
				}
			}, privacy2Index, privacy2Index + cusPrivacy2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置文字的前景色
			spanStr.setSpan(new ForegroundColorSpan(cusPrivacyColor2), privacy2Index, privacy2Index + cusPrivacy2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		if (!TextUtils.isEmpty(cusPrivacy3)) {
			int privacy3Index = ageementText.lastIndexOf(cusPrivacy3);
			//设置文字的单击事件
			spanStr.setSpan(new ClickableSpan() {
				@Override
				public void updateDrawState(TextPaint ds) {
					ds.setUnderlineText(false);
					ds.linkColor = Color.parseColor("#FFFFFF");
				}

				@Override
				public void onClick(View widget) {
					gotoAgreementPage("https://www.baidu.com", null);
				}
			}, privacy3Index, privacy3Index + cusPrivacy3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置文字的前景色
			spanStr.setSpan(new ForegroundColorSpan(cusPrivacyColor3), privacy3Index, privacy3Index + cusPrivacy3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return spanStr;
	}

	private static void gotoAgreementPage(String agreementUrl, String title) {
		if (TextUtils.isEmpty(agreementUrl)) {
			return;
		}
		AgreementPage page = new AgreementPage();
		Intent i = new Intent();
		i.putExtra("extra_agreement_url", agreementUrl);
		if (!TextUtils.isEmpty(title)) {
			i.putExtra("privacy",title);
		}
		page.show(MobSDK.getContext(), i);
	}

	@Deprecated
	public static UiSettings customizeUi0(){
		return new UiSettings.Builder()
				.setAgreementUncheckHintType(0)
				/** 标题栏 */
				// 标题栏背景色资源ID
				.setNavColorId(R.color.sec_verify_demo_text_color_blue)
				//标题栏是否透明
				.setNavTransparent(true)
				//标题栏是否隐藏
				.setNavHidden(false)
				//设置背景图片
//				.setBackgroundImgId(R.drawable.sec_verify_background_demo)
				//设置背景是否点击关闭页面
//				.setBackgroundClickClose(false)
				// 标题栏标题文字资源ID
//				.setNavTextId(R.string.sec_verify_demo_verify)
				// 标题栏文字颜色资源ID
//				.setNavTextColorId(R.color.sec_verify_demo_text_color_common_black)
				// 标题栏左侧关闭按钮图片资源ID
				.setNavCloseImgId(R.drawable.sec_verify_demo_close)
				//标题栏返回按钮是否隐藏
				.setNavCloseImgHidden(false)
				/** Logo */
				// Logo图片资源ID，默认使用应用图标
				.setLogoImgId(R.drawable.ic_launcher)
				//logo是否隐藏
				.setLogoHidden(false)
//				//logo宽度
				.setLogoWidth(R.dimen.sec_verify_demo_common_80)
				//logo高度
				.setLogoHeight(R.dimen.sec_verify_demo_common_80)
				//logo x轴偏移量
//				.setLogoOffsetX(R.dimen.sec_verify_demo_common_30)
				//logo y轴偏移量
//				.setLogoOffsetY(R.dimen.sec_verify_demo_common_300)
				/** 手机号 */
				// 脱敏手机号字体颜色资源ID
				.setNumberColorId(R.color.sec_verify_demo_text_color_common_black)
				// 脱敏手机号字体大小资源ID
				.setNumberSizeId(R.dimen.sec_verify_demo_text_size_m)
				//脱敏手机号 x轴偏移量
//				.setNumberOffsetX(R.dimen.sec_verify_demo_common_150)
				//脱敏手机号 y轴偏移量
//				.setNumberOffsetY(R.dimen.sec_verify_demo_common_300)
				/** 切换帐号 */
				// 切换账号字体颜色资源ID
				.setSwitchAccColorId(R.color.sec_verify_demo_text_color_blue)
				//切换账号 字体大小
				.setSwitchAccTextSize(R.dimen.sec_verify_demo_text_size_s)
				// 切换账号是否显示，默认显示
				.setSwitchAccHidden(false)
				//切换账号 x轴偏移量
//				.setSwitchAccOffsetX(R.dimen.sec_verify_demo_common_180)
				//切换账号 y轴偏移量
//				.setSwitchAccOffsetY(R.dimen.sec_verify_demo_switch_acc_offset_y)

				/** 登录按钮 */
				// 登录按钮背景图资源ID，建议使用shape
				.setLoginBtnImgId(R.drawable.sec_verify_demo_shape_rectangle)
				// 登录按钮文字资源ID
				.setLoginBtnTextId(R.string.sec_verify_demo_login)
				// 登录按钮字体颜色资源ID
				.setLoginBtnTextColorId(R.color.sec_verify_demo_text_color_common_white)
				//登录按钮字体大小
				.setLoginBtnTextSize(R.dimen.sec_verify_demo_text_size_s)
				//登录按钮 width
//				.setLoginBtnWidth(R.dimen.sec_verify_demo_common_100)
				//登录按钮 height
//				.setLoginBtnHeight(R.dimen.sec_verify_demo_common_80)
				//登录按钮 x轴偏移
//				.setLoginBtnOffsetX(R.dimen.sec_verify_demo_common_20)
				//登录按钮 y轴偏移
//				.setLoginBtnOffsetY(R.dimen.sec_verify_demo_common_400)
				/** 隐私协议 */
				//是否隐藏复选框(设置此属性true时setCheckboxDefaultState不会生效)
				.setCheckboxHidden(false)
				// 隐私协议复选框背景图资源ID，建议使用selector
				.setCheckboxImgId(R.drawable.sec_verify_demo_customized_checkbox_selector)
				// 隐私协议复选框默认状态，默认为“选中”
//				.setCheckboxDefaultState(true)
				// 隐私协议字体颜色资源ID（自定义隐私协议的字体颜色也受该值影响）
//				.setAgreementColorId(R.color.sec_verify_demo_main_color)
				// 自定义隐私协议一文字资源ID
//				.setCusAgreementNameId1(R.string.sec_verify_demo_customize_agreement_name_1)
//				// 自定义隐私协议一URL
//				.setCusAgreementUrl1("http://www.baidu.com")
////				自定义隐私协议一颜色
//                .setCusAgreementColor1(R.color.sec_verify_demo_main_color)
//				// 自定义隐私协议二文字资源ID
//				.setCusAgreementNameId2(R.string.sec_verify_demo_customize_agreement_name_2)
//				// 自定义隐私协议二URL
//				.setCusAgreementUrl2("https://www.jianshu.com")
////				自定义隐私协议二颜色
//                .setCusAgreementColor2(R.color.sec_verify_demo_main_color)
//					自定义隐私协议三
//				.setCusAgreementNameId3(R.string.sec_verify_demo_customize_agreement_name_3)
//				// 自定义隐私协议三URL
//				.setCusAgreementUrl3("https://www.jianshu.com")
//				自定义隐私协议三颜色
//                .setCusAgreementColor3(R.color.sec_verify_gui_text_color_common_gray_lighter)
				//隐私协议是否左对齐，默认居中
				.setAgreementGravityLeft(true)
				//隐私协议其他文字颜色
//				.setAgreementBaseTextColorId(R.color.sec_verify_demo_text_color_common_black)
				//隐私协议 x轴偏移量，默认30dp
				.setAgreementOffsetX(R.dimen.sec_verify_demo_common_50)
				//隐私协议 rightMargin右偏移量，默认30dp
				.setAgreementOffsetRightX(R.dimen.sec_verify_demo_common_50)
				//隐私协议 y轴偏移量
//				.setAgreementOffsetY(R.dimen.sec_verify_demo_common_50)
				//隐私协议 底部y轴偏移量
//				.setAgreementOffsetBottomY(R.dimen.sec_verify_demo_agreement_offset_bottom_y)
				/** slogan */
				//slogan文字大小
				.setSloganTextSize(R.dimen.sec_verify_demo_text_size_xs)
				//slogan文字颜色
				.setSloganTextColor(R.color.sec_verify_demo_text_color_common_gray)
				//slogan x轴偏移量
//				.setSloganOffsetX(R.dimen.sec_verify_demo_common_200)
				//slogan y轴偏移量
//				.setSloganOffsetY(R.dimen.sec_verify_demo_common_180)
				//slogan 底部y轴偏移量(设置此属性时，setSloganOffsetY不生效)
//				.setSloganOffsetBottomY(R.dimen.sec_verify_demo_common_20)
				//设置状态栏为透明状态栏，5.0以上生效
				.setImmersiveTheme(false)
				//设置状态栏文字颜色为黑色，只在6.0以上生效
				.setImmersiveStatusTextColorBlack(false)
				//使用平移动画
//				.setTranslateAnim(true)
				.setStartActivityTransitionAnim(R.anim.translate_in, R.anim.translate_out)
				.setFinishActivityTransitionAnim(R.anim.translate_in, R.anim.translate_out)
				//设置隐私协议文字起始
//				.setAgreementTextStart(R.string.sec_verify_demo_agreement_text_start)
//				//设置隐私协议文字连接
//				.setAgreementTextAnd1(R.string.sec_verify_demo_agreement_text_and1)
//				//设置隐私协议文字连接
//				.setAgreementTextAnd2(R.string.sec_verify_demo_agreement_text_and2)
//				//设置隐私协议文字结束
//				.setAgreementTextEnd(R.string.sec_verify_demo_agreement_text_end)
//				//设置移动隐私协议文字
//				.setAgreementCmccText(R.string.sec_verify_demo_agreement_text_cmcc)
//				//设置联通隐私协议文字
//				.setAgreementCuccText(R.string.sec_verify_demo_agreement_text_cucc)
//				//设置电信隐私协议文字
//				.setAgreementCtccText(R.string.sec_verify_demo_agreement_text_ctcc)
				//设置自定义的SpannableString
//				.setAgreementText(buildSpanString())
				//未选中复选框时点击登录的提示
//				.setAgreementUncheckHintText(R.string.ct_account_brand_text)
				//未选中复选框时点击登录的提示
				.setAgreementUncheckHintText("请阅读并勾选隐私协议")
				//未选中复选框时点击登录的提示类型，0为toast 1为弹窗
				.setAgreementUncheckHintType(0)
				//是否设置全屏
				.setFullScreen(false)
				//设置文字是否粗体
				.setNavTextBold(true)
				.setNumberBold(true)
				.setSwitchAccTextBold(true)
				.setAgreementTextBold(true)
				.setLoginBtnTextBold(true)
				.setSloganTextBold(true)
				//设置隐私协议和自定义隐私协议是否带下划线
				.setAgreementTextWithUnderLine(true)
				//设置隐私协议和自定义隐私协议是否带下划线
				.setAgreementUncheckToast(Toast.makeText(MobSDK.getContext(), "请先阅读隐私协议",Toast.LENGTH_SHORT))
				.setNavCloseImgScaleType(ImageView.ScaleType.CENTER_INSIDE)
				//设置打开隐私协议页面时标题
				.setAgreementPageTitle("title0")
				.setCusAgreementPageOneTitle("title1")
				.setCusAgreementPageTwoTitle("title2")
				.setCusAgreementPageThreeTitle("title3")
				//设置隐私协议显示页面图片和标题的参数
				.setAgreementPageCloseImg(MobSDK.getContext().getResources().getDrawable(R.drawable.sec_verify_demo_close))
				.setAgreementPageCloseImgScaleType(ImageView.ScaleType.FIT_CENTER)
				.setAgreementPageCloseImgHidden(false)
				.setAgreementPageCloseImgWidth(30)
				.setAgreementPageCloseImgHeight(30)
				.setAgreementPageTitleTextSize(16)
				.setAgreementPageTitleTextColor(0xff4E96FF)
				.setAgreementPageTitleTextBold(true)
				.setAgreementPageTitleHidden(false)
				//设置复选框缩放比例
				.setCheckboxScaleX(1.5f)
				.setCheckboxScaleY(1.5f)
				//设置复选框间距
				.setCheckboxOffsetX(10)
//				.setCheckboxOffsetRightX(30)
				.setCheckboxOffsetY(5)
//				.setCheckboxOffsetBottomY(15)
				.build();
	}

	@Deprecated
	public static UiSettings customizeUi1(){
		return new UiSettings.Builder()
				.setAgreementUncheckHintType(1)
				/** 标题栏 */
				// 标题栏背景色资源ID
				.setNavColorId(R.color.sec_verify_demo_text_color_blue)
				//标题栏是否透明
				.setNavTransparent(true)
				//标题栏是否隐藏
				.setNavHidden(false)
				//设置背景图片
//				.setBackgroundImgId(R.drawable.sec_verify_background_demo)
				//设置背景是否点击关闭页面
				.setBackgroundClickClose(false)
				// 标题栏标题文字资源ID
				.setNavTextId(R.string.sec_verify_demo_verify)
				//标题栏文字大小
				.setNavTextSize(R.dimen.sec_verify_demo_text_size_s)
				// 标题栏文字颜色资源ID
				.setNavTextColorId(R.color.sec_verify_demo_text_color_common_black)
				// 标题栏左侧关闭按钮图片资源ID
				.setNavCloseImgId(R.drawable.sec_verify_demo_close)
				//标题栏返回按钮是否隐藏
				.setNavCloseImgHidden(false)
				/** Logo */
				// Logo图片资源ID，默认使用应用图标
				.setLogoImgId(R.drawable.ic_launcher)
				//logo是否隐藏
				.setLogoHidden(false)
//				//logo宽度
				.setLogoWidth(R.dimen.sec_verify_demo_common_100)
				//logo高度
				.setLogoHeight(R.dimen.sec_verify_demo_common_100)
				//logo x轴偏移量
				.setLogoOffsetX(R.dimen.sec_verify_demo_common_30)
				//logo y轴偏移量
				.setLogoOffsetY(R.dimen.sec_verify_demo_common_30)
				//logo x轴右偏移量
				.setLogoOffsetRightX(R.dimen.sec_verify_demo_logo_offset_right_x_customize)
				//logo 是否靠屏幕右边
				.setLogoAlignParentRight(false)
				/** 手机号 */
				// 脱敏手机号字体颜色资源ID
				.setNumberColorId(R.color.sec_verify_demo_text_color_common_black)
				// 脱敏手机号字体大小资源ID
				.setNumberSizeId(R.dimen.sec_verify_demo_text_size_m)
				//脱敏手机号 x轴偏移量
				.setNumberOffsetX(R.dimen.sec_verify_demo_common_180)
				//脱敏手机号 y轴偏移量
				.setNumberOffsetY(R.dimen.sec_verify_demo_common_50)
				//脱敏手机号 x轴右偏移量
				.setNumberOffsetRightX(R.dimen.sec_verify_demo_common_30)
				//脱敏手机号 是否靠屏幕右边
				.setNumberAlignParentRight(true)
				/** 切换帐号 */
				// 切换账号字体颜色资源ID
				.setSwitchAccColorId(R.color.sec_verify_demo_text_color_blue)
				//切换账号 字体大小
				.setSwitchAccTextSize(R.dimen.sec_verify_demo_text_size_s)
				// 切换账号是否显示，默认显示
				.setSwitchAccHidden(false)
				//切换账号 x轴偏移量
				.setSwitchAccOffsetX(R.dimen.sec_verify_demo_common_200)
				//切换账号 y轴偏移量
				.setSwitchAccOffsetY(R.dimen.sec_verify_demo_common_100)
				////切换账号 文本内容
				.setSwitchAccText(R.string.sec_verify_demo_other_login)
				//脱敏手机号 x轴右偏移量
				.setSwitchAccOffsetRightX(R.dimen.sec_verify_demo_common_50)
				//脱敏手机号 是否靠屏幕右边
				.setSwitchAccAlignParentRight(true)
				/** 登录按钮 */
				// 登录按钮背景图资源ID，建议使用shape
				.setLoginBtnImgId(R.drawable.sec_verify_demo_shape_rectangle)
				// 登录按钮文字资源ID
				.setLoginBtnTextId(R.string.sec_verify_demo_login)
				// 登录按钮字体颜色资源ID
				.setLoginBtnTextColorId(R.color.sec_verify_demo_text_color_common_white)
				//登录按钮字体大小
				.setLoginBtnTextSize(R.dimen.sec_verify_demo_text_size_s)
				//登录按钮 width
				.setLoginBtnWidth(R.dimen.sec_verify_demo_common_200)
				//登录按钮 height
				.setLoginBtnHeight(R.dimen.sec_verify_demo_login_btn_height_customize)
				//登录按钮 x轴偏移
				.setLoginBtnOffsetX(R.dimen.sec_verify_demo_common_80)
				//登录按钮 y轴偏移
				.setLoginBtnOffsetY(R.dimen.sec_verify_demo_login_btn_offset_y_customize)
				//登录按钮 x轴右偏移
				.setLoginBtnOffsetRightX(R.dimen.sec_verify_demo_common_80)
				//登录按钮 靠屏幕右边
				.setLoginBtnAlignParentRight(true)
				/** 隐私协议 */
				//是否隐藏复选框(设置此属性true时setCheckboxDefaultState不会生效)
				.setCheckboxHidden(false)
				// 隐私协议复选框背景图资源ID，建议使用selector
				.setCheckboxImgId(R.drawable.sec_verify_demo_customized_checkbox_selector)
				// 隐私协议复选框默认状态，默认为“选中”
//				.setCheckboxDefaultState(true)
				// 隐私协议字体颜色资源ID（自定义隐私协议的字体颜色也受该值影响）
				.setAgreementColorId(R.color.sec_verify_demo_main_color)
				// 自定义隐私协议一文字资源ID
				.setCusAgreementNameId1(R.string.sec_verify_demo_customize_agreement_name_1)
				// 自定义隐私协议一URL
				.setCusAgreementUrl1("http://www.baidu.com")
//				自定义隐私协议一颜色
				.setCusAgreementColor1(R.color.sec_verify_demo_main_color)
				// 自定义隐私协议二文字资源ID
				.setCusAgreementNameId2(R.string.sec_verify_demo_customize_agreement_name_2)
				// 自定义隐私协议二URL
				.setCusAgreementUrl2("https://www.jianshu.com")
				//自定义隐私协议二颜色
				.setCusAgreementColor2(R.color.sec_verify_demo_main_color)
				//隐私协议是否左对齐，默认居中
				.setAgreementGravityLeft(true)
				//隐私协议其他文字颜色
				.setAgreementBaseTextColorId(R.color.sec_verify_demo_text_color_common_black)
				//隐私协议 x轴偏移量，默认30
				.setAgreementOffsetX(R.dimen.sec_verify_demo_common_50)
				//隐私协议 x轴rightMargin右偏移量，默认30
				.setAgreementOffsetRightX(R.dimen.sec_verify_demo_common_50)
				//隐私协议 y轴偏移量
				.setAgreementOffsetY(R.dimen.sec_verify_demo_agreement_offset_y_customize)
				//隐私协议 底部y轴偏移量
//				.setAgreementOffsetBottomY(R.dimen.sec_verify_demo_agreement_offset_bottom_y_customize)
				//隐私协议 靠屏幕右边
				.setAgreementAlignParentRight(true)
				//隐私协议 文字大小
				.setAgreementTextSize(R.dimen.sec_verify_demo_text_size_s)
				/** slogan */
				//slogan文字大小
				.setSloganTextSize(R.dimen.sec_verify_demo_text_size_s)
				//slogan文字颜色
				.setSloganTextColor(R.color.sec_verify_demo_main_color)
				//slogan x轴偏移量
//				.setSloganOffsetX(R.dimen.sec_verify_demo_common_200)
				//slogan y轴偏移量
				.setSloganOffsetY(R.dimen.sec_verify_demo_slogan_offset_y_customize)
				//slogan 底部y轴偏移量(设置此属性时，setSloganOffsetY不生效)
//				.setSloganOffsetBottomY(R.dimen.sec_verify_demo_common_30)
				//slogan x轴右偏移量
//				.setSloganOffsetRightX(R.dimen.sec_verify_demo_common_30)
				//slogan 靠屏幕右边
//				.setSloganAlignParentRight(true)
				//设置状态栏为透明状态栏，5.0以上生效
				.setImmersiveTheme(true)
				//设置状态栏文字颜色为黑色，只在6.0以上生效
				.setImmersiveStatusTextColorBlack(true)
//				.setZoomAnim(true)
				.setStartActivityTransitionAnim(R.anim.zoom_in, R.anim.zoom_out)
				.setFinishActivityTransitionAnim(R.anim.zoom_in, R.anim.zoom_out)
				.setBackgroundClickClose(true)
				.build();
	}

	@Deprecated
	public static UiSettings customizeUi2(){
		return new UiSettings.Builder()
				.setCheckboxDefaultState(true)
				.setLogoOffsetBottomY(R.dimen.sec_verify_demo_logo_offset_bottom_y_customize)
				.setNumberOffsetBottomY(R.dimen.sec_verify_demo_common_300)
				.setSwitchAccOffsetBottomY(R.dimen.sec_verify_demo_switch_acc_offset_bottom_y_customize)
				.setLoginBtnOffsetBottomY(R.dimen.sec_verify_demo_common_200)
				.setNumberOffsetRightX(R.dimen.sec_verify_demo_common_30)
				.setSwitchAccOffsetRightX(R.dimen.sec_verify_demo_common_50)
				.setLoginBtnOffsetRightX(R.dimen.sec_verify_demo_common_80)
				.setLoginBtnOffsetX(R.dimen.sec_verify_demo_common_80)
				.setSloganOffsetRightX(R.dimen.sec_verify_demo_common_30)
				.setNumberAlignParentRight(true)
				.setSwitchAccAlignParentRight(true)
				.setAgreementAlignParentRight(false)
				.setLoginBtnAlignParentRight(true)
				.setSloganAlignParentRight(true)
				.setBackgroundClickClose(false)
				.setImmersiveTheme(false)
				//设置状态栏文字颜色为黑色，只在6.0以上生效
				.setImmersiveStatusTextColorBlack(false)
				.setDialogMaskBackgroundClickClose(true)
				.setStartActivityTransitionAnim(R.anim.translate_bottom_in, R.anim.translate_bottom_out)
				.setFinishActivityTransitionAnim(R.anim.translate_bottom_in, R.anim.translate_bottom_out)
				.setDialogTheme(true)
				.setDialogAlignBottom(true)
				.setBottomTranslateAnim(true)
				.build();
	}

	@Deprecated
	public static UiSettings customizeUi3(){
		return new UiSettings.Builder()
				.setNavHidden(true)
				.setLogoHidden(true)
				.setNumberHidden(true)
				.setSwitchAccHidden(true)
				.setLoginBtnHidden(true)
				.setAgreementHidden(true)
				.setSloganHidden(true)
				.build();
	}

	@Deprecated
	public static LandUiSettings customizeUi5(Context context){
		return new LandUiSettings.Builder()
				.setNavColorId(0xffffffff)
				.setNavTextId("一键登录")
				.setNavTextColorId(0xff000000)
				.setNavCloseImgId(context.getResources().getDrawable(R.drawable.sec_verify_demo_close))
				.setNavHidden(false)
				.setNavTransparent( true)
				.setNavCloseImgHidden( false)
				.setNavTextSize(16)
				.setNavCloseImgWidth(30)
				.setNavCloseImgHeight(30)
				.setNavCloseImgOffsetX( 15)
//				.setNavCloseImgOffsetRightX( 30)
				.setNavCloseImgOffsetY(15)

				.setLogoImgId(context.getResources().getDrawable(R.drawable.sec_verify_demo_logo))
				.setLogoWidth( 80)
				.setLogoHeight( 80)
				.setLogoOffsetX( 150)
				.setLogoOffsetY( 30)
				.setLogoHidden( false)
//				.setLogoOffsetBottomY( 130)
				.setLogoOffsetRightX( 15)
				.setLogoAlignParentRight( false)

				.setNumberColorId(0xff000000)
				.setNumberSizeId(20)
				.setNumberOffsetX( 30)
				.setNumberOffsetY( 40)
				.setNumberHidden( false)
//				.setNumberOffsetBottomY( 110)
				.setNumberOffsetRightX( 150)
				.setNumberAlignParentRight( true)

				.setSwitchAccColorId(0xff4e96ff)
				.setSwitchAccTextSize(16)
				.setSwitchAccHidden(false)
				.setSwitchAccOffsetX(15)
				.setSwitchAccOffsetY(85)
				.setSwitchAccText( "其他方式登录")
//				.setSwitchAccOffsetBottomY( 90)
				.setSwitchAccOffsetRightX( 160)
				.setSwitchAccAlignParentRight( true)

				.setCheckboxImgId(context.getResources().getDrawable(R.drawable.sec_verify_demo_customized_checkbox_selector))
				.setCheckboxDefaultState(false)
				.setCheckboxHidden( false)

				.setAgreementColorId(0xfffe7a4e)
				.setAgreementOffsetX( 50)
				.setAgreementOffsetRightX( 50)
				.setAgreementOffsetY( 210)
//				.setAgreementOffsetBottomY( 15)
				.setAgreementGravityLeft( false)
				.setAgreementBaseTextColorId(0xff000000)
				.setAgreementTextSize( 15)
				.setAgreementCmccText( "《中国移动服务条款》")
				.setAgreementCuccText( "《中国联通服务条款》")
				.setAgreementCtccText( "《中国电信服务条款》")
				.setAgreementTextStart( "同意")
				.setAgreementTextAnd1( "和")
				.setAgreementTextAnd2( "、")
				.setAgreementTextEnd( "并使用本机号登录")
				.setAgreementHidden( false)
				.setAgreementAlignParentRight( false)

				.setCusAgreementNameId1( "隐私服务协议一")
				.setCusAgreementUrl1("http://baidu.com")
				.setCusAgreementNameId2( "隐私服务协议二")
				.setCusAgreementUrl2("http://baidu.com")
				.setCusAgreementColor1(0xfffe7a4e)
				.setCusAgreementColor2(0xfffe7a4e)

				.setLoginBtnImgId(context.getResources().getDrawable(R.drawable.sec_verify_demo_shape_rectangle))
				.setLoginBtnTextId( "登录")
				.setLoginBtnTextColorId( 0xffffffff)
				.setLoginBtnTextSize( 16)
				.setLoginBtnWidth( 200)
				.setLoginBtnHeight(40)
//				.setLoginBtnOffsetX( 15)
				.setLoginBtnOffsetY( 150)
//				.setLoginBtnOffsetRightX( 15)
//				.setLoginBtnAlignParentRight( false)
//				.setLoginBtnOffsetBottomY( 40)
				.setLoginBtnHidden( false)

				.setBackgroundImgId(context.getResources().getDrawable(R.color.sec_verify_demo_common_bg))
				.setBackgroundClickClose( false)

				.setSloganOffsetRightX( 15)
				.setSloganAlignParentRight( true)
				.setSloganOffsetX( 15)
				.setSloganOffsetY( 200)
				.setSloganOffsetBottomY(15)
				.setSloganTextSize( 16)
				.setSloganTextColor(0xffFF6347)
				.setSloganHidden( false)
				.setStartActivityTransitionAnim(R.anim.zoom_in, R.anim.zoom_out)
				.setFinishActivityTransitionAnim(R.anim.zoom_in, R.anim.zoom_out)
				.setImmersiveTheme( false)
				.setImmersiveStatusTextColorBlack( true)
//
//				.setDialogTheme( true)
//				.setDialogAlignBottom( false)
//				.setDialogOffsetX( 80)
//				.setDialogOffsetY( 80)
//				.setDialogWidth( 400)
//				.setDialogHeight( 400)
//				.setDialogMaskBackground(context.getResources().getDrawable(R.drawable.sec_verify_demo_common_progress_dialog_bg))
//				.setDialogMaskBackgroundClickClose( true)
//
//				.setTranslateAnim( true)
//				.setZoomAnim( true)
//				.setFadeAnim( true)
				.build();

	}

	@Deprecated
	public static List<View> buildCustomView2(Context context){
		View view = new View(context);
		view.setId(R.id.customized_view_id);
		view.setBackground(context.getResources().getDrawable(R.drawable.sec_verify_background_demo));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.topMargin = ResHelper.dipToPx(context,260);
		view.setLayoutParams(params);


		// 自定义按钮1
		ImageView btn1 = new ImageView(context);
		btn1.setId(R.id.customized_btn_id_1);
		btn1.setImageDrawable(context.getResources().getDrawable(R.drawable.sec_verify_demo_close));
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params1.topMargin = ResHelper.dipToPx(context,280);
		params1.leftMargin = ResHelper.dipToPx(context,15);
		btn1.setLayoutParams(params1);

		List<View> views = new ArrayList<View>();
		views.add(view);
		views.add(btn1);
		return views;
	}

	@Deprecated
	public static List<View> buildCustomView3(Context context){
		ImageView iv0 = new ImageView(context);
		iv0.setId(R.id.customized_btn_id_0);
		iv0.setImageDrawable(context.getResources().getDrawable(R.drawable.sec_verify_demo_close));
		RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params0.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params0.topMargin = ResHelper.dipToPx(context,15);
		params0.leftMargin = ResHelper.dipToPx(context,15);
		iv0.setLayoutParams(params0);


		EditText et1 = new EditText(context);
		et1.setId(R.id.customized_et_id_0);
		et1.setBackground(null);
		et1.setHint("请输入账号");
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params2.addRule(RelativeLayout.BELOW,iv0.getId());
		params2.topMargin = ResHelper.dipToPx(context,30);
		params2.leftMargin = ResHelper.dipToPx(context,15);
		et1.setLayoutParams(params2);

		View view0 = new View(context);
		view0.setId(R.id.customized_view_id_div);
		view0.setBackgroundColor(context.getResources().getColor(R.color.sec_verify_demo_text_color_common_gray));
		RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1);
		params4.addRule(RelativeLayout.BELOW,et1.getId());
		params4.leftMargin = ResHelper.dipToPx(context,15);
		params4.rightMargin = ResHelper.dipToPx(context,15);
		view0.setLayoutParams(params4);

		EditText et2 = new EditText(context);
		et2.setId(R.id.customized_et_id_1);
		et2.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
		et2.setHint("请输入密码");
		et2.setBackground(null);
		RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params3.addRule(RelativeLayout.BELOW,view0.getId());
		params3.topMargin = ResHelper.dipToPx(context,30);
		params3.leftMargin = ResHelper.dipToPx(context,15);
		et2.setLayoutParams(params3);

		View view1 = new View(context);
		view1.setId(R.id.customized_view_id_div1);
		view1.setBackgroundColor(context.getResources().getColor(R.color.sec_verify_demo_text_color_common_gray));
		RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1);
		params5.addRule(RelativeLayout.BELOW,et2.getId());
		params5.leftMargin = ResHelper.dipToPx(context,15);
		params5.rightMargin = ResHelper.dipToPx(context,15);
		view1.setLayoutParams(params5);

		Button button = new Button(context);
		button.setId(R.id.customized_btn_id_3);
		button.setText("登录");
		button.setBackground(context.getResources().getDrawable(R.drawable.sec_verify_demo_shape_rectangle));
		RelativeLayout.LayoutParams params6 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params6.leftMargin = ResHelper.dipToPx(context,30);
		params6.rightMargin = ResHelper.dipToPx(context,30);
		params6.topMargin = ResHelper.dipToPx(context,30);
		params6.addRule(RelativeLayout.BELOW,view1.getId());
		button.setLayoutParams(params6);

		TextView view = new TextView(context);
		view.setId(R.id.customized_view_id);
		view.setText("其他方式登录");
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.bottomMargin = ResHelper.dipToPx(context,150);
		view.setLayoutParams(params);

		ImageView iv1 = new ImageView(context);
		iv1.setId(R.id.customized_btn_id_1);
		iv1.setImageDrawable(context.getResources().getDrawable(R.drawable.sec_verify_demo_wechat));
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params1.bottomMargin = ResHelper.dipToPx(context,120);
		iv1.setLayoutParams(params1);


		List<View> views = new ArrayList<View>();

		views.add(iv0);
		views.add(view);
		views.add(iv1);
		views.add(et1);
		views.add(view0);
		views.add(et2);
		views.add(view1);
		views.add(button);
		return views;
	}


}

