#!/usr/bin/env python3
"""
Test script to verify the staff_id fix for Teacher Profile API
"""

import json
import os
import requests

def test_api_endpoints():
    """Test both GET and POST endpoints with the correct staff_id"""
    print("🔍 TESTING TEACHER PROFILE API ENDPOINTS")
    print("=" * 50)
    
    # Test data
    staff_id = "6"
    base_url = "https://school.cyberdetox.in/api"
    
    headers = {
        "Content-Type": "application/json",
        "Client-Service": "smartschool",
        "Auth-Key": "schoolAdmin@"
    }
    
    print(f"📋 Test Configuration:")
    print(f"   Staff ID: {staff_id}")
    print(f"   Base URL: {base_url}")
    print(f"   Headers: {headers}")
    
    # Test GET method
    print(f"\n🔗 Testing GET Method:")
    get_url = f"{base_url}/teacher/profile/{staff_id}"
    print(f"   URL: {get_url}")
    
    try:
        response = requests.get(get_url, headers=headers, timeout=10)
        print(f"   Status Code: {response.status_code}")
        
        if response.status_code == 200:
            data = response.json()
            print(f"   ✅ GET Success!")
            print(f"   Response Status: {data.get('status')}")
            print(f"   Message: {data.get('message')}")
            
            # Check payroll data
            payroll_details = data.get('payroll_details', {})
            payroll_records = payroll_details.get('payroll_records', [])
            print(f"   Payroll Records Count: {len(payroll_records)}")
            
            if payroll_records:
                latest = payroll_records[0]
                print(f"   Latest Payroll: {latest.get('month')} {latest.get('year')} - {latest.get('status')}")
            
        else:
            print(f"   ❌ GET Failed: {response.status_code}")
            print(f"   Response: {response.text}")
            
    except Exception as e:
        print(f"   ❌ GET Error: {str(e)}")
    
    # Test POST method
    print(f"\n📤 Testing POST Method:")
    post_url = f"{base_url}/teacher/profile"
    post_data = {"staff_id": int(staff_id)}
    print(f"   URL: {post_url}")
    print(f"   Data: {post_data}")
    
    try:
        response = requests.post(post_url, headers=headers, json=post_data, timeout=10)
        print(f"   Status Code: {response.status_code}")
        
        if response.status_code == 200:
            data = response.json()
            print(f"   ✅ POST Success!")
            print(f"   Response Status: {data.get('status')}")
            print(f"   Message: {data.get('message')}")
            
            # Check payroll data
            payroll_details = data.get('payroll_details', {})
            payroll_records = payroll_details.get('payroll_records', [])
            print(f"   Payroll Records Count: {len(payroll_records)}")
            
        else:
            print(f"   ❌ POST Failed: {response.status_code}")
            print(f"   Response: {response.text}")
            
    except Exception as e:
        print(f"   ❌ POST Error: {str(e)}")

def verify_android_fixes():
    """Verify the Android code fixes"""
    print(f"\n🔧 VERIFYING ANDROID CODE FIXES")
    print("=" * 40)
    
    files_to_check = [
        ("app/src/main/java/com/qdocs/ssre241123/utils/TeacherAuthHelper.java", [
            "getTeacherStaffId",
            "Constants.teacherStaffId"
        ]),
        ("app/src/main/java/com/qdocs/ssre241123/teachers/TeacherProfile.java", [
            "TeacherAuthHelper.getTeacherStaffId",
            "tryPostMethod",
            "Staff ID (for API)",
            "POST method as fallback"
        ])
    ]
    
    for file_path, expected_content in files_to_check:
        if os.path.exists(file_path):
            print(f"✅ {file_path}")
            
            with open(file_path, 'r') as f:
                content = f.read()
            
            for expected in expected_content:
                if expected in content:
                    print(f"   ✅ Contains: {expected}")
                else:
                    print(f"   ❌ Missing: {expected}")
        else:
            print(f"❌ File not found: {file_path}")

def simulate_android_behavior():
    """Simulate how the Android app will behave with the fixes"""
    print(f"\n📱 SIMULATING ANDROID APP BEHAVIOR")
    print("=" * 45)
    
    # Simulate SharedPreferences data (from login)
    mock_shared_prefs = {
        "teacherId": "123",  # Internal teacher ID
        "teacherStaffId": "6",  # Staff ID for API calls
        "teacherName": "MAHA LAKSHMI",
        "teacherToken": "dummy_token",
        "apiUrl": "https:school.cyberdetox.in/api/"
    }
    
    print("📋 Mock SharedPreferences Data:")
    for key, value in mock_shared_prefs.items():
        print(f"   {key}: {value}")
    
    # Simulate API call construction
    staff_id = mock_shared_prefs["teacherStaffId"]
    base_url = mock_shared_prefs["apiUrl"]
    api_url = f"{base_url}teacher/profile/{staff_id}"
    
    print(f"\n🔗 Simulated API Call:")
    print(f"   Staff ID used: {staff_id}")
    print(f"   API URL: {api_url}")
    print(f"   Headers will include:")
    print(f"     Client-Service: smartschool")
    print(f"     Auth-Key: schoolAdmin@")
    print(f"     User-ID: {staff_id}")
    print(f"     Content-Type: application/json")
    
    print(f"\n📊 Expected Behavior:")
    print(f"   1. Try GET request to: {api_url}")
    print(f"   2. If GET fails, try POST to: {base_url}teacher/profile")
    print(f"   3. POST will send staff_id: {staff_id} in request body")
    print(f"   4. Parse response and display payroll data")
    print(f"   5. Show 2 payroll records from your API response")

def generate_testing_instructions():
    """Generate testing instructions for the developer"""
    print(f"\n📋 TESTING INSTRUCTIONS")
    print("=" * 30)
    
    print("1. 🔧 Update API Base URL:")
    print("   - In the Android app, make sure apiUrl is set to:")
    print("   - 'https://school.cyberdetox.in/api/' (or your server URL)")
    
    print("\n2. 📱 Test Login Flow:")
    print("   - Login as teacher with staff_id 6")
    print("   - Verify SharedPreferences stores teacherStaffId correctly")
    
    print("\n3. 🔍 Check Debug Logs:")
    print("   - Use: adb logcat | grep 'Teacher Profile'")
    print("   - Look for: 'Staff ID (for API): 6'")
    print("   - Check: 'API CALL DEBUG' section")
    
    print("\n4. 📊 Expected Results:")
    print("   - Profile tab: Shows teacher information")
    print("   - Payroll tab: Shows 2 payroll records")
    print("   - Records: July 2025 (₹10,000) and October 2024 (₹10,000)")
    print("   - Status: 'generated' for both records")
    
    print("\n5. 🚨 If Still Not Working:")
    print("   - Check if localhost is accessible from device")
    print("   - Try using your computer's IP address instead")
    print("   - Verify the API server is running on port 80")
    print("   - Check firewall settings")

def main():
    print("🚀 TEACHER PROFILE STAFF_ID FIX VERIFICATION")
    print("=" * 60)
    
    test_api_endpoints()
    verify_android_fixes()
    simulate_android_behavior()
    generate_testing_instructions()
    
    print(f"\n✅ SUMMARY OF FIXES APPLIED:")
    print("=" * 35)
    print("✅ Added getTeacherStaffId() method to TeacherAuthHelper")
    print("✅ Updated TeacherProfile.java to use staff_id instead of teacherId")
    print("✅ Fixed User-ID header to use staff_id")
    print("✅ Added POST method fallback if GET fails")
    print("✅ Enhanced debug logging for API calls")
    print("✅ Improved error handling and headers")
    
    print(f"\n🎯 EXPECTED OUTCOME:")
    print("The Teacher Profile should now correctly call your API")
    print("with staff_id=6 and display the 2 payroll records!")

if __name__ == "__main__":
    main()
