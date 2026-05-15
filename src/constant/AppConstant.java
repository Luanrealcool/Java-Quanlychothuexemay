package constant;

import config.AppConfig;

public class AppConstant {
    public static final int PAGE_SIZE_DEFAULT = AppConfig.getInt("ui.page.size.default", 20);
    public static final Integer[] PAGE_SIZE_OPTIONS = {10, 20, 50, 100};

    public static final int PHU_PHI_TRE_PERCENT = AppConfig.getInt("business.phuphi.tre.percent", 150);
    public static final String HOP_DONG_PREFIX = AppConfig.get("business.hopdong.prefix", "HD");

    public static final String FORMAT_NGAY = "yyyy-MM-dd";
    public static final String FORMAT_NGAY_HIEN_THI = "dd/MM/yyyy";
    public static final String FORMAT_NGAY_GIO = "yyyy-MM-dd HH:mm:ss";
}
