package com.jieniuwuliu.jieniu.Util;


import android.util.Log;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class SignUtils {

	private static final String ALGORITHM = "RSA";
	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
	private static final String DEFAULT_CHARSET = "UTF-8";
	private static String s = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCHxsTpOjEYOWbAk6uUCt26O+ooLKwgW7p6TuSRJQE2XxKfMr2+L5OAatur/qY3bNB7FC53UCZONPp9cjMvDikGQ8O9TYTPpr6uJhs5iGRWb3negOIIq36keQ+N5rRFUXUP+rXOh4wvQIAJo7FXgfg0HWiSU6f9prV/XUjQm+nP9uCKUjPxPjyXstlGAGkYHUxYvFd5FWKIgoz4cD+uyKZQkzFM0Mv+KXkpIUuBazQQzpiAt1ZPfTmRSJlqjZ4O1UWFHYyQtAMTA3hU2Gp6ftvJJ3FI42R8eGUqHyRGi3CItWWcgTTRuDt+kJ29OsFIGq0i/tq1w/06WFl1UT0jf4iFAgMBAAECggEBAIGhsiDm0TbKnyLgGmG22ilTyOVgeVpPmcPqjOlTBkxOCy55uJTbQZ3K6D7XWwTx3D6WbyK1IE1s4Xdh0CJuB5cWncygNVw4XDdS5I1SflHUDuvz61rRuZD4kHQvvw+rjPObr3cnDR2UHGqXodrlmP7Q0SVbv7k6MHEQtFOPeHV3XyJ42i9tp+j/imkgSx2a0wt781wssERHeGL2ePdkWUWBWjs6gkg07YxDUGkLBeZQnoDe8YKESjzJyVE6bJTyQVAag7Xjl6MHjQGebibRAL0gooKHZEGAjUi7dWlO1iHY1zQk+vEPfCQDtQD6XY0nb7IBBzUOfdR/yeZCeSSCLwUCgYEAzPMjG+6q0eecaRdO4eBy/m63zOcyKrMk+xrj8cXG/2eF4ZIfKNA4c/ivcCkLGycyfZ8TIPlVLKdQh5uUyP6f6PCx3cEefFqEc6MDJE9YPfMoM2ep8fOoRcX3xZzhR7tpvBa+15KmvyqMUXdtxqv0iMCgoQH6yjfL4seBN+hiTzsCgYEAqZi21HPXa/x4qnLebxMlZfD+VuVE5bdPvxj++ROYnKuY6up9eYS0uAV+ipWlTHZSUGotHJLktTHtePfAZqozDZUc3zSxQ3K4srUVfSKGkI4LhGsJtM+IZNc8hHORXCIiSkzovaNxnHSeB8nu+dWmHsgJA5G7EoETR7sBc1RLiz8CgYEApI4jhm3e4aJhR0/bmiioq/khwndvJDGqY6N3mcv+t1rbd/3m3tIwQY1O73NE44xZUdvDdBo9dRjx46/binZG1rVLWo2QeLAE4QPlpeW/KB5wDLYTEZ1dYohNJHGNwBaOEuMAX/3zvFHhUBfgIEA4mwRu9FeukWazYWZmLgl7xbkCgYAm4GHwu4lDMMG8WM6SabIl9yNmafHsMqQuKx2vnQVbzJ4mI5CVvYzrrh6+x+T3bRwbYOtsZGPXAVXtZ+WL4zIGyAqxQ9PqoS6jkI0XfUWZd2Y+eYblErmyXAB8YD2PACgaHh0v83qEodrj8C8+x/HnlnUyg0J90GbKB50igc07YwKBgQCnOD7VPo8hg9I96i9SWTb3I3AAEjn2GpTyOTGYJAFfpGLGrWOwc4vKq+F8+gxVgM5ld1A6s7FVGEi42Ejg/jHC0pcbLAsk9BPVdPmW7pqqCsS/tgiT+qgy/wPI7CWDi16T88VHZ+jBdVKCriKq3XnJBenG2C29VdXrXz9MBsjsYA==";
	public static String sign(String content, String privateKey) {
		try {
			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);
			if (privateKey.equals(s)){
                Log.i("aaaaa","两个私钥一样");
            }
//			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
//			KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
//			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, "BC");
			PrivateKey priKey = keyFactory.generatePrivate(privSpec);
			signature.initSign(priKey);
			signature.update(content.getBytes(DEFAULT_CHARSET));
			byte[] signed = signature.sign();
			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
