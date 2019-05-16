package com.jieniuwuliu.jieniu.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtil {

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(147)|(15[^4,\\D])|(17[6-8])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
//		130、131、132、133、134、135、136、137、138、139
//		147
//		150、151、152、153、155、156、157、158、159、
//		180、181、182、183、184、185、186、187、188、189
//		176、177、178
	}

	public static boolean isIDNo(String idNo){
		String id18 = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)";
		String id15 = "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)";
		Pattern p = Pattern.compile(id18+"|"+id15);
		Matcher m = p.matcher(idNo);
		return m.matches();
	}

	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}
	
	public static boolean isUser(String user) {
		String str = "^[a-zA-Z0-9_]{4,20}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(user);
		return m.matches();
	}
	
	public static boolean isUsername(String username){
		String str = "^[a-zA-Z\u4E00-\u9FA5]{1,60}";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(username);
		return m.matches();
	}
	
	public static boolean isMsg(String msg){
		String str = "^[0-9a-zA-Z\u4E00-\u9FA5！@#￥%……&*（），。、？；‘：~·!$%(){},./?;':`~]{1,}";//^[0-9a-zA-Z\u4E00-\u9FA5！@#￥%……&*（），。、？；‘：~·!@#$%^&*(){},./?;':`~\"\[\]\”\“\【\】]{1,}
		Pattern p = Pattern.compile(str);//\"\[\]\”\“\【\】
		Matcher m = p.matcher(msg);
		return m.matches();//^[0-9a-zA-Z\u4E00-\u9FA5]{1,}
	}
	
	public static boolean isPwd(String pwd) {
//		String str = "^[a-zA-Z0-9]{6,12}$";
		String str = "^(?=.*[0-9])(?=.*[a-zA-Z])(.{6,15})$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(pwd);
		return m.matches();
	}
	
	public static boolean isNum(String num) {
		String str = "^[0-9]*$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(num);
		return m.matches();
	}
	
	public static boolean isS(String msg){
		String str = "^[0-9a-zA-Z]{6}";//^[0-9a-zA-Z\u4E00-\u9FA5！@#￥%……&*（），。、？；‘：~·!@#$%^&*(){},./?;':`~\"\[\]\”\“\【\】]{1,}
		Pattern p = Pattern.compile(str);//\"\[\]\”\“\【\】
		Matcher m = p.matcher(msg);
		return m.matches();//^[0-9a-zA-Z\u4E00-\u9FA5]{1,}
	}
	
	public static boolean isABCD(String string){
		String str = "^[ABCD]*$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(string);
		return m.matches();
	}
}
