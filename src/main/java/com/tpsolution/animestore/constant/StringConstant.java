package com.tpsolution.animestore.constant;

public class StringConstant {

    public static final String[] arrColBill = {"STT","TÊN SẢN PHẨM", "SỐ LƯỢNG","ĐƠN GIÁ","THÀNH TIỀN"};
    //30 minutes
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;
    //7 days
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;

   public class SortBy {
       public static final int SORT_BY_PRICE_ASC = 1;
       public static final int SORT_BY_PRICE_DESC = 2;
       public static final int SORT_BY_PRODUCT_NAME_ASC = 3;
       public static final int SORT_BY_PRODUCT_NAME_DESC = 4;
       public static final int SORT_BY_PRODUCT_OLDEST = 5;
       public static final int SORT_BY_PRODUCT_NEWEST = 6;
       public static final int SORT_BY_HOT_SALES = 7;
       public static final int SORT_BY_PRODUCT_QUANTITY_DESC = 8;
   }

    public class FilterBy {
        public static final int UNDER_500K = 1;
        public static final int FROM_500K_TO_1000K = 2;
        public static final int FROM_1000K_TO_1500K = 3;
        public static final int FROM_1500K_TO_2000K = 4;
        public static final int OVER_2000K = 5;

        public static final int _500K = 500000;
        public static final int _1000K = 1000000;
        public static final int _1500K = 1500000;
        public static final int _2000K = 2000000;

    }

    public class TemplateEmail {
        public static final String TEMPLATE_EMAIL_VERIFY_ACCOUNT = "email_verification";
        public static final String TEMPLATE_EMAIL_FORGOT_PASSWORD = "email_forgot_pw";
        public static final String SHOP_LOGO_IMAGE = "../static/images/logo_tpstore.png";
        public static final String MAIL_SUBJECT_VERIFY_ACCOUNT = "Registration Confirmation";
        public static final String MAIL_SUBJECT_FORGOT_PASSWORD = "Reset Password Confirmation";

    }

}
