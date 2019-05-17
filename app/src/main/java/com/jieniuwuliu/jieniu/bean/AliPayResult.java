package com.jieniuwuliu.jieniu.bean;

public class AliPayResult {

    /**
     * status : 0
     * msg : 成功
     * data : {"PrivateKey":"MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCMjOaKXBHwIF1uDRhAfKRnG4x3qj+OK4+h6R/hznHXnWNsGQshx0GJQwyimLzkRME0vPhb2K2x5MtzrodrmcCDO1WN0q7G4HubXe09+HsGAmsil4z7sxnOhHAaxxaWBpgscpqbkrfnZiqCDdgU6JwFakhy/AkBqXM/8SIz3w2shREx3cm5oU1Kve1dieUx7wCdeLNE2m/ff6ROHJIkI7B65yWLpG2wk2zKJD3ZYR8Oyn42CCo/w1Z3olYCPa5XCwFD8qSZil+4gbFh3lXIuudAkV6SM88cUoJ7XFrbFUgEHnAntsi77dL9hfibQp930p5trE6EcvMU1YY0SnV23CB/AgMBAAECggEAZrEcUMNsp9I98YbHzXZEeMF8JILT5QCW9WnrzYSVUUDBv92ccJWxoKTIupR2jyJdODdKD3GFl5Dfxioi789rBXd4zyK8aLrkFWTPTpQa7w0dUi0A6Xa2yFmjvyzvp7YxwWgK5tiQXhYbwOJ0mvJJ7Pr27GyR+BDjEDkTEGtNBlsstIxievE4k1pGW0tNGW53X18izz6nl449ziaENlggqOQMKGtmCbeoQAZ7S/gLurFZxOYdkAZUhXPX2wOWkQDNtLmHlAVgCkVzO9LJ2UoDGOeF7HodGEgfazQLl0NcQOOycmT9qHVuaKoQDgMkSref0siMmkiitT3mwsdXi6JQgQKBgQDS0EsEpqp7fHaQRRBGECPsyTGDBZR3xZd3RDBmj4/JWwkDUUPV3zQZcdtJEpAoWVFZz+7N7tBYcLtF+lq8qkyFR/boAbNNEKbMf+OG3mS7XkZgtvzRjSyyh9QuvbPpB0k4DG24kEszc2zzbl7efriieBksd08eW/AvBleYPxHj0QKBgQCqrSRitOSfnLFsl2LjIecLdTVSAFnMnRgdJckWD/0k0Jb1cgCKMIxJqL3//Zux+/PTVmyp2MmRDNMEkxWjs60a7lKJbo/cRoYdpzPwqRd371LjkvndGvTXnFkcQDNlVm+1QwsL3UOYIQfYHeG1vGlJo15mnjAZw7EpPafKUa1jTwKBgG23MhPNmdSCxkH3b6zHIatOWpJSCR78mwPbXUVDWhX1Zw9u79sTO04yz2mMW7JkKc6QRTywGYatxQl6blbaWtDSz1GX8PUewXCU7pl2jP5m5kHXemSlEKopvuhEmqmgvR6KeWAg2MKCRCW9P2qUkQsdbhySS38FYk9XAaeey3TBAoGANg89VR5iw8OH1xNkOPkGC3UGfICr8MlZV/Id7SUHU1/WUMvZK+wQo0wVUj3r0MYzBp97L5kAzza2p95GnmFM1Rjjf7BLVMPpuv64zuNgEZb2NCrbhZMCNkDJYM9c+B4Wp90iZJGi1U6xA10DnuKcnrxmLC6yoZaASMXEnaqdNrUCgYAdlPuSlxEt36jfnh6+FotSdm4JvGMYQpCO0pvmSUbqh0E24KeKp68fR8jrBSu/XvY8ky22zZpOgsDPJ1mFIExa8MvBFFU9TMWmgxKcYotn/hPeOemi1VMDKkE4Y3ro8jpO/dN2vVZcZYonAV8fCbKIKg5mN09sdIidenPndvIXEw==","Appid":"2019051264467257","Value":"app_id=2019051264467257&biz_content=%7B%22subject%22%3A%22%E5%BF%AB%E9%80%92%E8%B4%B9%22%2C%22out_trade_no%22%3A%221128675224531767296%22%2C%22total_amount%22%3A%220.01%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=utf-8&method=alipay.trade.app.pay&sign_type=RSA2&timestamp=2019-05-15+14%3A55%3A38&version=1.0"}
     */

    private int status;
    private String msg;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * PrivateKey : MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCMjOaKXBHwIF1uDRhAfKRnG4x3qj+OK4+h6R/hznHXnWNsGQshx0GJQwyimLzkRME0vPhb2K2x5MtzrodrmcCDO1WN0q7G4HubXe09+HsGAmsil4z7sxnOhHAaxxaWBpgscpqbkrfnZiqCDdgU6JwFakhy/AkBqXM/8SIz3w2shREx3cm5oU1Kve1dieUx7wCdeLNE2m/ff6ROHJIkI7B65yWLpG2wk2zKJD3ZYR8Oyn42CCo/w1Z3olYCPa5XCwFD8qSZil+4gbFh3lXIuudAkV6SM88cUoJ7XFrbFUgEHnAntsi77dL9hfibQp930p5trE6EcvMU1YY0SnV23CB/AgMBAAECggEAZrEcUMNsp9I98YbHzXZEeMF8JILT5QCW9WnrzYSVUUDBv92ccJWxoKTIupR2jyJdODdKD3GFl5Dfxioi789rBXd4zyK8aLrkFWTPTpQa7w0dUi0A6Xa2yFmjvyzvp7YxwWgK5tiQXhYbwOJ0mvJJ7Pr27GyR+BDjEDkTEGtNBlsstIxievE4k1pGW0tNGW53X18izz6nl449ziaENlggqOQMKGtmCbeoQAZ7S/gLurFZxOYdkAZUhXPX2wOWkQDNtLmHlAVgCkVzO9LJ2UoDGOeF7HodGEgfazQLl0NcQOOycmT9qHVuaKoQDgMkSref0siMmkiitT3mwsdXi6JQgQKBgQDS0EsEpqp7fHaQRRBGECPsyTGDBZR3xZd3RDBmj4/JWwkDUUPV3zQZcdtJEpAoWVFZz+7N7tBYcLtF+lq8qkyFR/boAbNNEKbMf+OG3mS7XkZgtvzRjSyyh9QuvbPpB0k4DG24kEszc2zzbl7efriieBksd08eW/AvBleYPxHj0QKBgQCqrSRitOSfnLFsl2LjIecLdTVSAFnMnRgdJckWD/0k0Jb1cgCKMIxJqL3//Zux+/PTVmyp2MmRDNMEkxWjs60a7lKJbo/cRoYdpzPwqRd371LjkvndGvTXnFkcQDNlVm+1QwsL3UOYIQfYHeG1vGlJo15mnjAZw7EpPafKUa1jTwKBgG23MhPNmdSCxkH3b6zHIatOWpJSCR78mwPbXUVDWhX1Zw9u79sTO04yz2mMW7JkKc6QRTywGYatxQl6blbaWtDSz1GX8PUewXCU7pl2jP5m5kHXemSlEKopvuhEmqmgvR6KeWAg2MKCRCW9P2qUkQsdbhySS38FYk9XAaeey3TBAoGANg89VR5iw8OH1xNkOPkGC3UGfICr8MlZV/Id7SUHU1/WUMvZK+wQo0wVUj3r0MYzBp97L5kAzza2p95GnmFM1Rjjf7BLVMPpuv64zuNgEZb2NCrbhZMCNkDJYM9c+B4Wp90iZJGi1U6xA10DnuKcnrxmLC6yoZaASMXEnaqdNrUCgYAdlPuSlxEt36jfnh6+FotSdm4JvGMYQpCO0pvmSUbqh0E24KeKp68fR8jrBSu/XvY8ky22zZpOgsDPJ1mFIExa8MvBFFU9TMWmgxKcYotn/hPeOemi1VMDKkE4Y3ro8jpO/dN2vVZcZYonAV8fCbKIKg5mN09sdIidenPndvIXEw==
         * Appid : 2019051264467257
         * Value : app_id=2019051264467257&biz_content=%7B%22subject%22%3A%22%E5%BF%AB%E9%80%92%E8%B4%B9%22%2C%22out_trade_no%22%3A%221128675224531767296%22%2C%22total_amount%22%3A%220.01%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=utf-8&method=alipay.trade.app.pay&sign_type=RSA2&timestamp=2019-05-15+14%3A55%3A38&version=1.0
         */

        private String PrivateKey;
        private String Appid;
        private String out_trade_no;
        private String Notify;

        public String getPrivateKey() {
            return PrivateKey;
        }

        public void setPrivateKey(String PrivateKey) {
            this.PrivateKey = PrivateKey;
        }

        public String getAppid() {
            return Appid;
        }

        public void setAppid(String Appid) {
            this.Appid = Appid;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getNotify() {
            return Notify;
        }

        public void setNotify(String notify) {
            Notify = notify;
        }
    }
}
