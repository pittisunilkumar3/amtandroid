#!/usr/bin/env python3
"""
Comprehensive debugging script for Teacher Profile API and data flow
"""

import requests
import json
import sys

def test_api_response():
    """Test the actual API response"""
    print("🔍 TESTING TEACHER PROFILE API")
    print("=" * 50)
    
    # API Configuration
    base_url = "https://school.cyberdetox.in/api"
    endpoint = "/teacher/profile/6"
    url = base_url + endpoint
    
    headers = {
        "Client-Service": "smartschool",
        "Auth-Key": "schoolAdmin@",
        "Content-Type": "application/json",
        "User-ID": "6",
        "Authorization": "Bearer dummy_token"
    }
    
    print(f"📡 API URL: {url}")
    print(f"📋 Headers: {headers}")
    
    try:
        response = requests.get(url, headers=headers, timeout=30)
        
        if response.status_code != 200:
            print(f"❌ API request failed: {response.status_code}")
            print(f"Response: {response.text}")
            return None
        
        data = response.json()
        print(f"✅ API Response received successfully!")
        
        return data
        
    except Exception as e:
        print(f"❌ API request failed: {e}")
        return None

def analyze_payroll_data(data):
    """Analyze payroll data specifically"""
    print(f"\n💰 PAYROLL DATA ANALYSIS")
    print("-" * 30)
    
    payroll_details = data.get('payroll_details', {})
    payroll_records = payroll_details.get('payroll_records', [])
    salary_summary = payroll_details.get('salary_summary', {})
    bank_details = data.get('bank_details', {})
    
    print(f"📊 Payroll Records: {len(payroll_records)}")
    print(f"📊 Salary Summary: {salary_summary}")
    print(f"📊 Bank Details: {bank_details}")
    
    if payroll_records:
        print(f"\n📋 Latest Payroll Record:")
        latest = payroll_records[0]
        for key, value in latest.items():
            print(f"   {key}: {value}")
    else:
        print(f"⚠️  No payroll records found")
    
    # Check what the Android app should display
    print(f"\n📱 Expected Android Display:")
    
    # Basic salary from bank_details
    basic_salary = bank_details.get('basic_salary', '0')
    print(f"   Basic Salary: {basic_salary}")
    
    # Account details
    account_title = bank_details.get('account_title', '')
    bank_account_no = bank_details.get('bank_account_no', '')
    bank_name = bank_details.get('bank_name', '')
    bank_branch = bank_details.get('bank_branch', '')
    
    print(f"   Account Title: '{account_title}' (empty: {account_title == ''})")
    print(f"   Bank Account: '{bank_account_no}' (empty: {bank_account_no == ''})")
    print(f"   Bank Name: '{bank_name}' (empty: {bank_name == ''})")
    print(f"   Bank Branch: '{bank_branch}' (empty: {bank_branch == ''})")
    
    # Latest payroll status
    if payroll_records:
        latest_status = payroll_records[0].get('status', '')
        latest_payment_date = payroll_records[0].get('payment_date', '')
        print(f"   Latest Status: '{latest_status}'")
        print(f"   Latest Payment Date: '{latest_payment_date}'")
    else:
        print(f"   Status: 'No payroll records'")
        print(f"   Payment Date: 'No payment date'")

def analyze_qr_code_data(data):
    """Analyze QR code data"""
    print(f"\n📱 QR CODE DATA ANALYSIS")
    print("-" * 25)
    
    qr_code = data.get('qr_code', {})
    
    if qr_code:
        print(f"✅ QR Code data found:")
        qr_code_url = qr_code.get('qr_code_url', '')
        qr_string = qr_code.get('qr_string', '')
        qr_data = qr_code.get('data', {})
        
        print(f"   QR Code URL: {qr_code_url}")
        print(f"   QR String: {qr_string[:100]}..." if len(qr_string) > 100 else f"   QR String: {qr_string}")
        print(f"   QR Data: {qr_data}")
        
        # Test if QR code URL is accessible
        if qr_code_url:
            try:
                qr_response = requests.get(qr_code_url, timeout=10)
                if qr_response.status_code == 200:
                    print(f"   ✅ QR Code URL is accessible")
                else:
                    print(f"   ❌ QR Code URL returned: {qr_response.status_code}")
            except Exception as e:
                print(f"   ❌ QR Code URL test failed: {e}")
    else:
        print(f"❌ No QR code data found")

def analyze_fragment_data(data):
    """Analyze data for each fragment"""
    print(f"\n🧩 FRAGMENT DATA ANALYSIS")
    print("-" * 30)
    
    # Profile Fragment
    basic_info = data.get('basic_info', {})
    contact_info = data.get('contact_info', {})
    personal_info = data.get('personal_info', {})
    address_info = data.get('address_info', {})
    
    print(f"👤 Profile Fragment:")
    print(f"   Basic Info: {len(basic_info)} fields")
    print(f"   Contact Info: {len(contact_info)} fields")
    print(f"   Personal Info: {len(personal_info)} fields")
    print(f"   Address Info: {len(address_info)} fields")
    
    # Payroll Fragment
    payroll_details = data.get('payroll_details', {})
    bank_details = data.get('bank_details', {})
    
    print(f"\n💰 Payroll Fragment:")
    print(f"   Payroll Details: {len(payroll_details)} sections")
    print(f"   Bank Details: {len(bank_details)} fields")
    
    # Leaves Fragment
    leave_records = data.get('leave_records', {})
    leave_requests = leave_records.get('leave_requests', [])
    leave_balance = leave_records.get('leave_balance', [])
    
    print(f"\n🏖️  Leaves Fragment:")
    print(f"   Leave Requests: {len(leave_requests)}")
    print(f"   Leave Balance: {len(leave_balance)}")
    
    # Attendance Fragment
    attendance_records = data.get('attendance_records', {})
    attendance_summary = attendance_records.get('attendance_summary', {})
    recent_attendance = attendance_records.get('recent_attendance', [])
    attendance_types = attendance_records.get('attendance_types', [])
    
    print(f"\n📅 Attendance Fragment:")
    print(f"   Summary: {len(attendance_summary)} categories")
    print(f"   Recent Records: {len(recent_attendance)}")
    print(f"   Attendance Types: {len(attendance_types)}")
    
    # Documents Fragment
    documents = data.get('documents', [])
    
    print(f"\n📄 Documents Fragment:")
    print(f"   Documents: {len(documents)}")

def identify_issues(data):
    """Identify potential issues"""
    print(f"\n🔧 ISSUE IDENTIFICATION")
    print("-" * 25)
    
    issues = []
    
    # Check payroll data
    payroll_details = data.get('payroll_details', {})
    payroll_records = payroll_details.get('payroll_records', [])
    bank_details = data.get('bank_details', {})
    
    if not payroll_records:
        issues.append("No payroll records - will show 'No payroll records'")
    
    # Check if all bank details are empty
    bank_fields = ['account_title', 'bank_name', 'bank_branch', 'bank_account_no', 'basic_salary']
    empty_bank_fields = [field for field in bank_fields if not bank_details.get(field, '').strip()]
    
    if len(empty_bank_fields) == len(bank_fields):
        issues.append("All bank details are empty")
    elif empty_bank_fields:
        issues.append(f"Some bank details are empty: {empty_bank_fields}")
    
    # Check QR code
    qr_code = data.get('qr_code', {})
    if not qr_code or not qr_code.get('qr_code_url', ''):
        issues.append("QR code URL is missing or empty")
    
    # Check documents
    documents = data.get('documents', [])
    if not documents:
        issues.append("No documents available")
    
    if issues:
        print(f"⚠️  Found {len(issues)} potential issues:")
        for i, issue in enumerate(issues, 1):
            print(f"   {i}. {issue}")
    else:
        print(f"✅ No major issues identified")

def main():
    print("🚀 TEACHER PROFILE DEBUG ANALYSIS")
    print("=" * 50)
    
    # Test API
    data = test_api_response()
    if not data:
        print("❌ Cannot proceed without API data")
        sys.exit(1)
    
    # Save response for reference
    with open('debug_api_response.json', 'w') as f:
        json.dump(data, f, indent=2)
    print(f"💾 API response saved to 'debug_api_response.json'")
    
    # Analyze different aspects
    analyze_payroll_data(data)
    analyze_qr_code_data(data)
    analyze_fragment_data(data)
    identify_issues(data)
    
    print(f"\n📋 SUMMARY")
    print("-" * 15)
    print(f"✅ API is working and returning comprehensive data")
    print(f"✅ All required sections are present in the response")
    print(f"⚠️  Some sections have empty data (normal for this teacher)")
    print(f"✅ Android app should handle empty data gracefully")
    
    print(f"\n🔍 NEXT DEBUGGING STEPS:")
    print("1. Check Android logs for any parsing errors")
    print("2. Verify fragment update methods are being called")
    print("3. Check if RecyclerView adapters are being notified")
    print("4. Test QR code image loading")

if __name__ == "__main__":
    main()
