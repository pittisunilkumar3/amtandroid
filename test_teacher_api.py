#!/usr/bin/env python3
"""
Test script for Teacher Profile API
Tests the comprehensive teacher profile endpoint to verify data structure
"""

import requests
import json
import sys

def test_teacher_profile_api():
    """Test the teacher profile API endpoint"""
    
    # API Configuration
    base_url = "https://school.cyberdetox.in/api"
    endpoint = "/teacher/profile/6"  # Using staff_id 6 from the example
    url = base_url + endpoint
    
    # Headers as specified in the API documentation
    headers = {
        "Client-Service": "smartschool",
        "Auth-Key": "schoolAdmin@",
        "Content-Type": "application/json",
        "User-ID": "6",
        "Authorization": "Bearer dummy_token"  # This might need a real token
    }
    
    print(f"Testing Teacher Profile API...")
    print(f"URL: {url}")
    print(f"Headers: {json.dumps(headers, indent=2)}")
    print("-" * 50)
    
    try:
        # Make the API request
        response = requests.get(url, headers=headers, timeout=30)
        
        print(f"Status Code: {response.status_code}")
        print(f"Response Headers: {dict(response.headers)}")
        print("-" * 50)
        
        if response.status_code == 200:
            try:
                data = response.json()
                print("✅ API Response received successfully!")
                print(f"Response structure analysis:")
                print("-" * 30)
                
                # Analyze the response structure
                analyze_response_structure(data)
                
                # Save full response for detailed analysis
                with open('teacher_profile_response.json', 'w') as f:
                    json.dump(data, f, indent=2)
                print(f"\n📁 Full response saved to 'teacher_profile_response.json'")
                
            except json.JSONDecodeError as e:
                print(f"❌ Failed to parse JSON response: {e}")
                print(f"Raw response: {response.text[:500]}...")
                
        else:
            print(f"❌ API request failed with status {response.status_code}")
            print(f"Response: {response.text}")
            
    except requests.exceptions.RequestException as e:
        print(f"❌ Request failed: {e}")
        return False
    
    return True

def analyze_response_structure(data):
    """Analyze the structure of the API response"""
    
    if not isinstance(data, dict):
        print("❌ Response is not a JSON object")
        return
    
    print(f"📊 Top-level keys: {list(data.keys())}")
    
    # Check for required sections
    required_sections = [
        'basic_info', 'contact_info', 'personal_info', 'address_info',
        'bank_details', 'payroll_details', 'leave_records', 
        'attendance_records', 'documents', 'qr_code', 'profile_image'
    ]
    
    print(f"\n🔍 Section Analysis:")
    for section in required_sections:
        if section in data:
            section_data = data[section]
            if isinstance(section_data, dict):
                keys = list(section_data.keys()) if section_data else []
                print(f"  ✅ {section}: {len(keys)} keys - {keys[:3]}{'...' if len(keys) > 3 else ''}")
            elif isinstance(section_data, list):
                print(f"  ✅ {section}: Array with {len(section_data)} items")
            else:
                print(f"  ✅ {section}: {type(section_data).__name__} - {str(section_data)[:50]}...")
        else:
            print(f"  ❌ {section}: MISSING")
    
    # Detailed analysis of critical sections
    print(f"\n📋 Detailed Section Analysis:")
    
    # Payroll Details
    if 'payroll_details' in data:
        payroll = data['payroll_details']
        if isinstance(payroll, dict):
            records = payroll.get('payroll_records', [])
            print(f"  💰 Payroll: {len(records)} records, salary_summary: {'✅' if 'salary_summary' in payroll else '❌'}")
        else:
            print(f"  💰 Payroll: Invalid structure - {type(payroll)}")
    
    # Leave Records
    if 'leave_records' in data:
        leaves = data['leave_records']
        if isinstance(leaves, dict):
            requests_count = len(leaves.get('leave_requests', []))
            balance_count = len(leaves.get('leave_balance', []))
            print(f"  🏖️  Leaves: {requests_count} requests, {balance_count} balance entries")
        else:
            print(f"  🏖️  Leaves: Invalid structure - {type(leaves)}")
    
    # Attendance Records
    if 'attendance_records' in data:
        attendance = data['attendance_records']
        if isinstance(attendance, dict):
            summary = attendance.get('attendance_summary', {})
            recent = attendance.get('recent_attendance', [])
            print(f"  📅 Attendance: Summary with {len(summary)} stats, {len(recent)} recent records")
        else:
            print(f"  📅 Attendance: Invalid structure - {type(attendance)}")
    
    # Documents
    if 'documents' in data:
        docs = data['documents']
        if isinstance(docs, list):
            print(f"  📄 Documents: {len(docs)} documents")
        else:
            print(f"  📄 Documents: Invalid structure - {type(docs)}")

if __name__ == "__main__":
    print("🚀 Teacher Profile API Test")
    print("=" * 50)
    
    success = test_teacher_profile_api()
    
    if success:
        print("\n✅ Test completed successfully!")
    else:
        print("\n❌ Test failed!")
        sys.exit(1)
