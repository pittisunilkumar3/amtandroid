package com.qdocs.ssre241123.utils;

import com.qdocs.ssre241123.R;
import java.util.HashMap;
import java.util.Map;

public class FontAwesomeIconMapper {

    private static final Map<String, Integer> iconMap = new HashMap<>();

    static {
        // Initialize the FontAwesome icon mappings
        
        // Front Office & Administrative
        iconMap.put("fa fa-ioxhost ftlayer", R.drawable.ic_fa_building); // Front Office icon
        iconMap.put("fa fa-ioxhost", R.drawable.ic_fa_building);
        
        // User Management
        iconMap.put("fa fa-user-plus ftlayer", R.drawable.ic_fa_user);
        iconMap.put("fa fa-user-plus", R.drawable.ic_fa_user);
        iconMap.put("fa fa-user", R.drawable.ic_fa_user);
        iconMap.put("fa fa-money ftlayer", R.drawable.ic_fa_money);
        iconMap.put("fa fa-money", R.drawable.ic_fa_money);
        iconMap.put("fa fa-usd ftlayer", R.drawable.ic_fa_dollar);
        iconMap.put("fa fa-usd", R.drawable.ic_fa_dollar);
        iconMap.put("fa fa-dollar", R.drawable.ic_fa_dollar);
        iconMap.put("fa fa-credit-card ftlayer", R.drawable.ic_fa_credit_card);
        iconMap.put("fa fa-credit-card", R.drawable.ic_fa_credit_card);
        iconMap.put("fa fa-credit-card-alt", R.drawable.ic_fa_credit_card_alt);
        iconMap.put("fa fa-line-chart ftlayer", R.drawable.ic_fa_calculator);
        iconMap.put("fa fa-line-chart", R.drawable.ic_fa_calculator);
        iconMap.put("fa fa-calculator", R.drawable.ic_fa_calculator);
        iconMap.put("fa fa-calendar-check-o ftlayer", R.drawable.ic_fa_calendar_check);
        iconMap.put("fa fa-calendar-check-o", R.drawable.ic_fa_calendar_check);
        iconMap.put("fa fa-calendar-check", R.drawable.ic_fa_calendar_check);
        iconMap.put("fa fa-mortar-board ftlayer", R.drawable.ic_fa_graduation_cap);
        iconMap.put("fa fa-mortar-board", R.drawable.ic_fa_graduation_cap);
        iconMap.put("fa fa-graduation-cap", R.drawable.ic_fa_graduation_cap);
        iconMap.put("fa fa-sitemap ftlayer", R.drawable.ic_fa_sitemap);
        iconMap.put("fa fa-sitemap", R.drawable.ic_fa_sitemap);
        iconMap.put("fa fa-bullhorn ftlayer", R.drawable.ic_fa_envelope);
        iconMap.put("fa fa-bullhorn", R.drawable.ic_fa_envelope);
        iconMap.put("fa fa-envelope", R.drawable.ic_fa_envelope);
        iconMap.put("fa fa-book ftlayer", R.drawable.ic_fa_book);
        iconMap.put("fa fa-book", R.drawable.ic_fa_book);
        iconMap.put("fa fa-object-group ftlayer", R.drawable.ic_fa_archive);
        iconMap.put("fa fa-object-group", R.drawable.ic_fa_archive);
        iconMap.put("fa fa-archive", R.drawable.ic_fa_archive);
        iconMap.put("fa fa-bus ftlayer", R.drawable.ic_fa_bus);
        iconMap.put("fa fa-bus", R.drawable.ic_fa_bus);
        iconMap.put("fa fa-building-o ftlayer", R.drawable.ic_fa_building);
        iconMap.put("fa fa-building-o", R.drawable.ic_fa_building);
        iconMap.put("fa fa-building", R.drawable.ic_fa_building);
        iconMap.put("fa fa-newspaper-o ftlayer", R.drawable.ic_fa_certificate);
        iconMap.put("fa fa-newspaper-o", R.drawable.ic_fa_certificate);
        iconMap.put("fa fa-certificate", R.drawable.ic_fa_certificate);
        iconMap.put("fa fa-gears ftlayer", R.drawable.ic_fa_cogs);
        iconMap.put("fa fa-gears", R.drawable.ic_fa_cogs);
        iconMap.put("fa fa-cogs", R.drawable.ic_fa_cogs);
        iconMap.put("fa fa-video-camera", R.drawable.ic_videocam);
        iconMap.put("fa fa-file-text", R.drawable.ic_fa_file_text);
        iconMap.put("fa fa-file-text-o", R.drawable.ic_fa_file_text);
        iconMap.put("fa fa-file-text-o ftlayer", R.drawable.ic_fa_file_text);
        iconMap.put("fa fa-check-circle ftlayer", R.drawable.ic_fa_certificate);
        iconMap.put("fa fa-check-circle", R.drawable.ic_fa_certificate);
        iconMap.put("fa fa-universal-access ftlayer", R.drawable.ic_fa_users);
        iconMap.put("fa fa-universal-access", R.drawable.ic_fa_users);
        iconMap.put("fa fa-empire ftlayer", R.drawable.ic_fa_building);
        iconMap.put("fa fa-empire", R.drawable.ic_fa_building);
        iconMap.put("fa fa-list-alt ftlayer", R.drawable.ic_fa_list_alt);
        iconMap.put("fa fa-flask ftlayer", R.drawable.ic_fa_tasks);
        iconMap.put("fa fa-flask", R.drawable.ic_fa_tasks);
        iconMap.put("fa fa-download ftlayer", R.drawable.ic_download);
        iconMap.put("fa fa-download", R.drawable.ic_download);
        iconMap.put("fa fa-rss ftlayer", R.drawable.ic_fa_rss);
        iconMap.put("fa fa-map-o ftlayer", R.drawable.ic_fa_exclamation_triangle);
        iconMap.put("fa fa-map-o", R.drawable.ic_fa_exclamation_triangle);
        iconMap.put("fa fa-map-signs ftlayer", R.drawable.ic_fa_exclamation_triangle);
        iconMap.put("fa fa-map-signs", R.drawable.ic_fa_exclamation_triangle);
        iconMap.put("fa fa-exclamation-triangle", R.drawable.ic_fa_exclamation_triangle);
        iconMap.put("fa fa-plus-square", R.drawable.ic_fa_id_card);
        iconMap.put("fa fa-id-card", R.drawable.ic_fa_id_card);
        iconMap.put("fa fa-list-ol", R.drawable.ic_fa_ticket);
        iconMap.put("fa fa-ticket", R.drawable.ic_fa_ticket);
        iconMap.put("fa fa-address-card", R.drawable.ic_fa_list_alt);
        iconMap.put("fa fa-list-alt", R.drawable.ic_fa_list_alt);
        iconMap.put("fa fa-upload", R.drawable.ic_fa_upload);
        iconMap.put("fa fa-share-alt", R.drawable.ic_fa_share_alt);
        iconMap.put("fa fa-percent", R.drawable.ic_fa_percent);
        iconMap.put("fa fa-users", R.drawable.ic_fa_users);
        iconMap.put("fa fa-address-book", R.drawable.ic_fa_address_book);
        iconMap.put("fa fa-desktop", R.drawable.ic_fa_desktop);
        iconMap.put("fa fa-download", R.drawable.ic_download);
        iconMap.put("fa fa-bar-chart", R.drawable.ic_fa_bar_chart);
        iconMap.put("fa fa-rss", R.drawable.ic_fa_rss);
        iconMap.put("fa fa-tasks", R.drawable.ic_fa_tasks);

        // ===== Remix Icons (ri-*) - Used by web sidebar =====
        // Front Office
        iconMap.put("ri-building-2-line", R.drawable.ic_fa_building);
        iconMap.put("ri-building-2-fill", R.drawable.ic_fa_building);
        // Student Information
        iconMap.put("ri-user-3-line", R.drawable.ic_fa_user);
        iconMap.put("ri-user-3-fill", R.drawable.ic_fa_user);
        iconMap.put("ri-user-add-line", R.drawable.ic_fa_user);
        iconMap.put("ri-user-add-fill", R.drawable.ic_fa_user);
        // Fees
        iconMap.put("ri-wallet-3-line", R.drawable.ic_fa_money);
        iconMap.put("ri-wallet-3-fill", R.drawable.ic_fa_money);
        // Income
        iconMap.put("ri-funds-line", R.drawable.ic_fa_dollar);
        iconMap.put("ri-funds-fill", R.drawable.ic_fa_dollar);
        // Expense
        iconMap.put("ri-wallet-2-line", R.drawable.ic_fa_credit_card);
        iconMap.put("ri-wallet-2-fill", R.drawable.ic_fa_credit_card);
        // Attendance
        iconMap.put("ri-calendar-check-line", R.drawable.ic_fa_calendar_check);
        iconMap.put("ri-calendar-check-fill", R.drawable.ic_fa_calendar_check);
        // CBSE Examination
        iconMap.put("ri-medal-line", R.drawable.ic_fa_graduation_cap);
        iconMap.put("ri-medal-fill", R.drawable.ic_fa_graduation_cap);
        // Examinations
        iconMap.put("ri-file-list-3-line", R.drawable.ic_fa_file_text);
        iconMap.put("ri-file-list-3-fill", R.drawable.ic_fa_file_text);
        // Online Examinations
        iconMap.put("ri-macbook-line", R.drawable.ic_fa_desktop);
        iconMap.put("ri-macbook-fill", R.drawable.ic_fa_desktop);
        // Lesson Plan
        iconMap.put("ri-task-line", R.drawable.ic_fa_tasks);
        iconMap.put("ri-task-fill", R.drawable.ic_fa_tasks);
        // Academics
        iconMap.put("ri-graduation-cap-line", R.drawable.ic_fa_graduation_cap);
        iconMap.put("ri-graduation-cap-fill", R.drawable.ic_fa_graduation_cap);
        // Human Resource
        iconMap.put("ri-group-line", R.drawable.ic_fa_users);
        iconMap.put("ri-group-fill", R.drawable.ic_fa_users);
        // Communicate
        iconMap.put("ri-message-3-line", R.drawable.ic_fa_envelope);
        iconMap.put("ri-message-3-fill", R.drawable.ic_fa_envelope);
        iconMap.put("ri-notification-3-line", R.drawable.ic_fa_bell);
        iconMap.put("ri-notification-3-fill", R.drawable.ic_fa_bell);
        // Download Center
        iconMap.put("ri-download-2-line", R.drawable.ic_download);
        iconMap.put("ri-download-2-fill", R.drawable.ic_download);
        // Homework
        iconMap.put("ri-book-open-line", R.drawable.ic_fa_book);
        iconMap.put("ri-book-open-fill", R.drawable.ic_fa_book);
        // Library
        iconMap.put("ri-book-2-line", R.drawable.ic_fa_book);
        iconMap.put("ri-book-2-fill", R.drawable.ic_fa_book);
        // Inventory
        iconMap.put("ri-archive-drawer-line", R.drawable.ic_fa_archive);
        iconMap.put("ri-archive-drawer-fill", R.drawable.ic_fa_archive);
        // Transport
        iconMap.put("ri-bus-line", R.drawable.ic_fa_bus);
        iconMap.put("ri-bus-fill", R.drawable.ic_fa_bus);
        // Hostel
        iconMap.put("ri-hotel-line", R.drawable.ic_fa_home);
        iconMap.put("ri-hotel-fill", R.drawable.ic_fa_home);
        // Certificate
        iconMap.put("ri-award-line", R.drawable.ic_fa_certificate);
        iconMap.put("ri-award-fill", R.drawable.ic_fa_certificate);
        // Front CMS
        iconMap.put("ri-layout-3-line", R.drawable.ic_fa_sitemap);
        iconMap.put("ri-layout-3-fill", R.drawable.ic_fa_sitemap);
        // Alumni
        iconMap.put("ri-user-star-line", R.drawable.ic_fa_graduation_cap);
        iconMap.put("ri-user-star-fill", R.drawable.ic_fa_graduation_cap);
        // Reports
        iconMap.put("ri-bar-chart-2-line", R.drawable.ic_fa_bar_chart);
        iconMap.put("ri-bar-chart-2-fill", R.drawable.ic_fa_bar_chart);
        // System Settings
        iconMap.put("ri-settings-3-line", R.drawable.ic_fa_cogs);
        iconMap.put("ri-settings-3-fill", R.drawable.ic_fa_cogs);
        // Behaviour Records
        iconMap.put("ri-clipboard-line", R.drawable.ic_fa_file_text);
        iconMap.put("ri-clipboard-fill", R.drawable.ic_fa_file_text);
        // Multi Branch
        iconMap.put("ri-node-tree", R.drawable.ic_fa_sitemap);
        // Fee Discount
        iconMap.put("ri-price-tag-3-line", R.drawable.ic_fa_money);
        iconMap.put("ri-price-tag-3-fill", R.drawable.ic_fa_money);
        // Referral
        iconMap.put("ri-user-shared-line", R.drawable.ic_fa_users);
        // Admission No / Hall Ticket
        iconMap.put("ri-id-card-line", R.drawable.ic_fa_id_card);
        iconMap.put("ri-id-card-fill", R.drawable.ic_fa_id_card);
        iconMap.put("ri-coupon-line", R.drawable.ic_fa_ticket);
        iconMap.put("ri-coupon-fill", R.drawable.ic_fa_ticket);
        // Results
        iconMap.put("ri-bar-chart-box-line", R.drawable.ic_fa_bar_chart);
        // TC Generation
        iconMap.put("ri-file-text-line", R.drawable.ic_fa_file_text);
        // Importing
        iconMap.put("ri-download-cloud-2-line", R.drawable.ic_download);
        // Zoom / Gmeet
        iconMap.put("ri-vidicon-line", R.drawable.ic_videocam);
        iconMap.put("ri-video-chat-line", R.drawable.ic_videocam);
        // Other Fees
        iconMap.put("ri-bill-line", R.drawable.ic_fa_money);
        // Accounting
        iconMap.put("ri-calculator-line", R.drawable.ic_fa_calculator);
        // HallTicket Generation
        iconMap.put("ri-ticket-2-line", R.drawable.ic_fa_ticket);
        // Face Attendance
        iconMap.put("ri-user-follow-line", R.drawable.ic_fa_user);
        // Generate Paper
        iconMap.put("ri-sparkling-line", R.drawable.ic_fa_certificate);
        // Student Resume
        iconMap.put("ri-file-text-o", R.drawable.ic_fa_file_text);
        // General Remix icon fallbacks
        iconMap.put("ri-user-line", R.drawable.ic_fa_user);
        iconMap.put("ri-search-line", R.drawable.ic_fa_search);
        iconMap.put("ri-search-eye-line", R.drawable.ic_fa_search);
        iconMap.put("ri-add-line", R.drawable.ic_fa_plus);
        iconMap.put("ri-add-circle-line", R.drawable.ic_fa_plus);
        iconMap.put("ri-edit-line", R.drawable.ic_fa_edit);
        iconMap.put("ri-delete-bin-line", R.drawable.ic_fa_trash);
        iconMap.put("ri-close-line", R.drawable.ic_fa_times);
        iconMap.put("ri-arrow-left-line", R.drawable.ic_arrow_back);
        iconMap.put("ri-arrow-right-line", R.drawable.ic_arrow_forward);
        iconMap.put("ri-home-line", R.drawable.ic_home_black_24dp);
        iconMap.put("ri-home-fill", R.drawable.ic_home_black_24dp);
        iconMap.put("ri-time-line", R.drawable.ic_fa_calendar);
        iconMap.put("ri-mail-line", R.drawable.ic_fa_envelope);
        iconMap.put("ri-phone-line", R.drawable.ic_fa_phone);
        iconMap.put("ri-global-line", R.drawable.ic_fa_globe);
        iconMap.put("ri-lock-line", R.drawable.ic_fa_lock);
        iconMap.put("ri-notification-line", R.drawable.ic_fa_bell);
        iconMap.put("ri-gallery-line", R.drawable.ic_fa_image);
        iconMap.put("ri-image-line", R.drawable.ic_fa_image);
        iconMap.put("ri-map-pin-line", R.drawable.ic_fa_map_marker);
        iconMap.put("ri-star-line", R.drawable.ic_fa_star);
        iconMap.put("ri-heart-line", R.drawable.ic_fa_heart);
        iconMap.put("ri-check-line", R.drawable.ic_fa_check);
        iconMap.put("ri-error-warning-line", R.drawable.ic_fa_exclamation_triangle);
        iconMap.put("ri-information-line", R.drawable.ic_fa_info_circle);
        iconMap.put("ri-question-line", R.drawable.ic_fa_question_circle);
        iconMap.put("ri-refresh-line", R.drawable.ic_fa_refresh);
        iconMap.put("ri-save-line", R.drawable.ic_fa_save);
        iconMap.put("ri-printer-line", R.drawable.ic_fa_print);
        iconMap.put("ri-external-link-line", R.drawable.ic_fa_external_link);
        iconMap.put("ri-links-line", R.drawable.ic_fa_link);
        iconMap.put("ri-file-line", R.drawable.ic_fa_file);
        iconMap.put("ri-folder-line", R.drawable.ic_fa_folder);
        iconMap.put("ri-folder-open-line", R.drawable.ic_fa_folder_open);
        iconMap.put("ri-calendar-line", R.drawable.ic_fa_calendar);
        iconMap.put("ri-dashboard-line", R.drawable.ic_fa_bar_chart);
        iconMap.put("ri-pie-chart-line", R.drawable.ic_fa_pie_chart);
        iconMap.put("ri-bar-chart-line", R.drawable.ic_fa_bar_chart);
        iconMap.put("ri-line-chart-line", R.drawable.ic_fa_line_chart);
        iconMap.put("ri-team-line", R.drawable.ic_fa_users);
        iconMap.put("ri-shield-check-line", R.drawable.ic_fa_shield);
        iconMap.put("ri-eye-line", R.drawable.ic_fa_eye);
        iconMap.put("ri-camera-line", R.drawable.ic_fa_camera);
        iconMap.put("ri-mic-line", R.drawable.ic_fa_microphone);
        iconMap.put("ri-volume-up-line", R.drawable.ic_fa_volume_up);
        iconMap.put("ri-customer-service-2-line", R.drawable.ic_fa_headset);
        iconMap.put("ri-money-dollar-circle-line", R.drawable.ic_fa_money);
        iconMap.put("ri-bank-card-line", R.drawable.ic_fa_credit_card);
        iconMap.put("ri-hand-coin-line", R.drawable.ic_fa_money);
        iconMap.put("ri-exchange-dollar-line", R.drawable.ic_fa_exchange);
        iconMap.put("ri-refund-2-line", R.drawable.ic_fa_undo);
        iconMap.put("ri-file-copy-line", R.drawable.ic_fa_copy);
        iconMap.put("ri-checkbox-circle-line", R.drawable.ic_fa_check_circle);
        iconMap.put("ri-close-circle-line", R.drawable.ic_fa_times_circle);
        iconMap.put("ri-add-circle-fill", R.drawable.ic_fa_plus_circle);
        iconMap.put("ri-subtract-line", R.drawable.ic_fa_minus);
        iconMap.put("ri-checkbox-line", R.drawable.ic_fa_check_square);
        iconMap.put("ri-indeterminate-circle-line", R.drawable.ic_fa_spinner);
        iconMap.put("ri-loader-4-line", R.drawable.ic_fa_spinner);
        // Student Resume - fa icon
        iconMap.put("fa fa-file-text-o", R.drawable.ic_fa_file_text);
        // WhatsApp
        iconMap.put("ri-whatsapp-line", R.drawable.ic_fa_share_alt);
        iconMap.put("ri-chat-check-line", R.drawable.ic_fa_comment);

        // Add fallback icons for any missing mappings
        iconMap.put("default", R.drawable.ic_fa_cogs);
    }

    /**
     * Get drawable resource ID for FontAwesome icon class
     * @param fontAwesomeClass FontAwesome class name (e.g., "fa fa-user")
     * @return Drawable resource ID
     */
    public static int getDrawableResource(String fontAwesomeClass) {
        if (fontAwesomeClass == null || fontAwesomeClass.isEmpty()) {
            return iconMap.get("default");
        }

        // Try exact match first
        Integer resourceId = iconMap.get(fontAwesomeClass.trim());
        if (resourceId != null) {
            return resourceId;
        }

        // Try without "ftlayer" suffix
        String cleanedClass = fontAwesomeClass.replace(" ftlayer", "").trim();
        resourceId = iconMap.get(cleanedClass);
        if (resourceId != null) {
            return resourceId;
        }

        // Return default icon if no match found
        return iconMap.get("default");
    }

    /**
     * Check if an icon mapping exists for the given FontAwesome class
     * @param fontAwesomeClass FontAwesome class name
     * @return true if mapping exists, false otherwise
     */
    public static boolean hasIconMapping(String fontAwesomeClass) {
        if (fontAwesomeClass == null || fontAwesomeClass.isEmpty()) {
            return false;
        }
        
        return iconMap.containsKey(fontAwesomeClass.trim()) || 
               iconMap.containsKey(fontAwesomeClass.replace(" ftlayer", "").trim());
    }
}
