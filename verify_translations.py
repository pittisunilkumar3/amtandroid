#!/usr/bin/env python3
"""
Verification script to check if Hindi translations are complete
"""

import re
import xml.etree.ElementTree as ET

def extract_string_names(file_path):
    """Extract all string names from a strings.xml file"""
    try:
        tree = ET.parse(file_path)
        root = tree.getroot()
        
        string_names = []
        for string_elem in root.findall('string'):
            name = string_elem.get('name')
            if name:
                string_names.append(name)
        
        return set(string_names)
    except Exception as e:
        print(f"Error parsing {file_path}: {e}")
        return set()

def main():
    print("🔍 TRANSLATION VERIFICATION")
    print("=" * 40)
    
    # Extract string names from both files
    main_strings = extract_string_names('app/src/main/res/values/strings.xml')
    hindi_strings = extract_string_names('app/src/main/res/values-hi/strings.xml')
    
    print(f"📊 String counts:")
    print(f"   Main (English): {len(main_strings)} strings")
    print(f"   Hindi: {len(hindi_strings)} strings")
    
    # Find missing strings
    missing_in_hindi = main_strings - hindi_strings
    extra_in_hindi = hindi_strings - main_strings
    
    if missing_in_hindi:
        print(f"\n❌ Missing in Hindi ({len(missing_in_hindi)} strings):")
        for string_name in sorted(missing_in_hindi):
            print(f"   - {string_name}")
    else:
        print(f"\n✅ All strings are translated to Hindi!")
    
    if extra_in_hindi:
        print(f"\n⚠️  Extra in Hindi ({len(extra_in_hindi)} strings):")
        for string_name in sorted(extra_in_hindi):
            print(f"   - {string_name}")
    
    # Summary
    print(f"\n📈 TRANSLATION SUMMARY")
    print("-" * 25)
    
    if not missing_in_hindi and not extra_in_hindi:
        print("✅ Perfect match! All strings are properly translated.")
        return True
    elif not missing_in_hindi:
        print("✅ All required strings are translated (some extras exist).")
        return True
    else:
        print(f"❌ {len(missing_in_hindi)} strings still need translation.")
        return False

if __name__ == "__main__":
    success = main()
    if success:
        print(f"\n🎉 Translation verification PASSED!")
        print("   The Hindi localization errors should now be resolved.")
    else:
        print(f"\n⚠️  Translation verification FAILED!")
        print("   Some strings still need to be added to the Hindi file.")
