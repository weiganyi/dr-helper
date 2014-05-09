package com.drhelper.util;

public class PrefsManager {
	private static String server_address = "172.16.3.146";;
	private static boolean empty_table_notice = false;
	private static boolean finish_menu_notice = false;
	private static boolean notice_service_start = false;

	public static String getServer_address() {
		return server_address;
	}
	public static void setServer_address(String server_address) {
		PrefsManager.server_address = server_address;
	}
	public static boolean isEmpty_table_notice() {
		return empty_table_notice;
	}
	public static void setEmpty_table_notice(boolean empty_table_notice) {
		PrefsManager.empty_table_notice = empty_table_notice;
	}
	public static boolean isFinish_menu_notice() {
		return finish_menu_notice;
	}
	public static void setFinish_menu_notice(boolean finish_menu_notice) {
		PrefsManager.finish_menu_notice = finish_menu_notice;
	}
	public static boolean isNotice_service_start() {
		return notice_service_start;
	}
	public static void setNotice_service_start(boolean notice_service_start) {
		PrefsManager.notice_service_start = notice_service_start;
	}
}
