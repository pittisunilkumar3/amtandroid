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
