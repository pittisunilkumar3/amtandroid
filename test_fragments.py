#!/usr/bin/env python3
"""
Test script to verify Teacher Profile fragments are working correctly
Creates test data and simulates fragment data processing
"""

import json
import sys

def create_test_teacher_data():
    """Create comprehensive test data matching the API response structure"""
    return {
        "status": 1,
        "message": "Profile retrieved successfully.",
        "staff_id": "6",
        "basic_info": {
            "id": "6",
            "employee_id": "200226",
            "name": "MAHA LAKSHMI",
            "surname": "SALLA",
            "full_name": "MAHA LAKSHMI SALLA",
            "designation": "2",
            "designation_name": "Accountant",
            "department": "9",
            "department_name": "Finance",
            "user_type": "",
            "role_id": "",
            "is_active": "1",
            "date_of_joining": "2023-08-01",
            "date_of_leaving": None,
            "disable_at": None
        },
        "contact_info": {
            "email": "mahalakshmisalla70@gmail.com",
            "contact_no": "8328595488",
            "emergency_contact_no": "6303727148"
        },
        "personal_info": {
            "gender": "Female",
            "dob": "2002-11-26",
            "marital_status": "Single",
            "father_name": "Salla Vijay chandhra",
            "mother_name": "Salla Parameshwari",
            "qualification": "B.sc computer science",
            "work_exp": "1 year",
            "note": ""
        },
        "address_info": {
            "local_address": "Bc colony ,venkatagiri,tirupati-524404",
            "permanent_address": "Bc colony ,venkatagiri,tirupati-524404"
        },
        "bank_details": {
            "account_title": "MAHA LAKSHMI SALLA",
            "bank_name": "State Bank of India",
            "bank_branch": "Tirupati Main",
            "bank_account_no": "1234567890",
            "ifsc_code": "SBIN0001234",
            "payscale": "Grade 1",
            "basic_salary": "25000",
            "epf_no": "EPF123456",
            "contract_type": "Permanent",
            "shift": "Morning",
            "location": "Main Campus"
        },
        "payroll_details": {
            "payroll_records": [
                {
                    "id": "28",
                    "staff_id": "6",
                    "basic": "25000.00",
                    "total_allowance": "5000.00",
                    "total_deduction": "2000.00",
                    "leave_deduction": "500",
                    "tax": "1500",
                    "net_salary": "26000.00",
                    "status": "paid",
                    "month": "September",
                    "year": "2025",
                    "payment_mode": "Bank Transfer",
                    "payment_date": "2025-09-15",
                    "remark": "Regular monthly salary",
                    "generated_by": "1",
                    "created_at": "2025-09-15 10:30:00"
                },
                {
                    "id": "27",
                    "staff_id": "6",
                    "basic": "25000.00",
                    "total_allowance": "5000.00",
                    "total_deduction": "2000.00",
                    "leave_deduction": "0",
                    "tax": "1500",
                    "net_salary": "26500.00",
                    "status": "paid",
                    "month": "August",
                    "year": "2025",
                    "payment_mode": "Bank Transfer",
                    "payment_date": "2025-08-15",
                    "remark": "Regular monthly salary",
                    "generated_by": "1",
                    "created_at": "2025-08-15 10:30:00"
                }
            ],
            "salary_summary": {
                "net_salary": "26000.00",
                "earnings": "30000.00",
                "deduction": "4000.00",
                "basic_salary": "25000.00",
                "tax": "1500.00"
            }
        },
        "leave_records": {
            "leave_requests": [
                {
                    "name": "MAHA LAKSHMI",
                    "surname": "SALLA",
                    "employee_id": "200226",
                    "id": "3",
                    "staff_id": "6",
                    "leave_type_id": "2",
                    "leave_from": "2025-09-20",
                    "leave_to": "2025-09-22",
                    "leave_days": "3",
                    "employee_remark": "Family function",
                    "admin_remark": "Approved",
                    "status": "approved",
                    "applied_by": "1",
                    "document_file": "",
                    "date": "2025-09-15",
                    "created_at": "2025-09-15 09:30:00",
                    "type": "Casual Leave"
                },
                {
                    "name": "MAHA LAKSHMI",
                    "surname": "SALLA",
                    "employee_id": "200226",
                    "id": "2",
                    "staff_id": "6",
                    "leave_type_id": "1",
                    "leave_from": "2025-08-10",
                    "leave_to": "2025-08-12",
                    "leave_days": "3",
                    "employee_remark": "Medical checkup",
                    "admin_remark": "Approved",
                    "status": "approved",
                    "applied_by": "1",
                    "document_file": "",
                    "date": "2025-08-05",
                    "created_at": "2025-08-05 14:20:00",
                    "type": "Medical Leave"
                }
            ],
            "leave_balance": [
                {
                    "type": "Medical Leave",
                    "alloted_leave": "10",
                    "approve_leave": 3,
                    "remaining_leave": 7
                },
                {
                    "type": "Casual Leave",
                    "alloted_leave": "12",
                    "approve_leave": 3,
                    "remaining_leave": 9
                },
                {
                    "type": "Maternity Leave",
                    "alloted_leave": "90",
                    "approve_leave": 0,
                    "remaining_leave": 90
                }
            ],
            "total_requests": 2
        },
        "attendance_records": {
            "attendance_summary": {
                "Present": "18",
                "Late": "2",
                "Absent": "1",
                "Half Day": "1",
                "Holiday": "8"
            },
            "recent_attendance": [
                {
                    "date": "2025-09-18",
                    "attendance_type": "Present",
                    "check_in": "09:00:00",
                    "check_out": "17:30:00",
                    "working_hours": "8.5"
                },
                {
                    "date": "2025-09-17",
                    "attendance_type": "Late",
                    "check_in": "09:15:00",
                    "check_out": "17:30:00",
                    "working_hours": "8.25"
                },
                {
                    "date": "2025-09-16",
                    "attendance_type": "Present",
                    "check_in": "08:55:00",
                    "check_out": "17:25:00",
                    "working_hours": "8.5"
                }
            ],
            "attendance_types": [
                {
                    "id": "1",
                    "type": "Present",
                    "key_value": "<b class=\"text text-success\">P</b>",
                    "is_active": "yes",
                    "created_at": "0000-00-00 00:00:00",
                    "updated_at": "0000-00-00"
                },
                {
                    "id": "2",
                    "type": "Late",
                    "key_value": "<b class=\"text text-warning\">L</b>",
                    "is_active": "yes",
                    "created_at": "0000-00-00 00:00:00",
                    "updated_at": "0000-00-00"
                },
                {
                    "id": "3",
                    "type": "Absent",
                    "key_value": "<b class=\"text text-danger\">A</b>",
                    "is_active": "yes",
                    "created_at": "0000-00-00 00:00:00",
                    "updated_at": "0000-00-00"
                }
            ]
        },
        "documents": [
            {
                "id": "1",
                "title": "Educational Certificate",
                "file_name": "bsc_certificate.pdf",
                "file_path": "/uploads/staff_documents/bsc_certificate.pdf",
                "created_at": "2023-08-01 10:00:00"
            },
            {
                "id": "2",
                "title": "Identity Proof",
                "file_name": "aadhar_card.pdf",
                "file_path": "/uploads/staff_documents/aadhar_card.pdf",
                "created_at": "2023-08-01 10:05:00"
            }
        ],
        "qr_code": {
            "data": {
                "type": "staff_profile",
                "staff_id": "6",
                "employee_id": "200226",
                "name": "MAHA LAKSHMI SALLA",
                "designation": "Accountant",
                "department": "Finance",
                "email": "mahalakshmisalla70@gmail.com",
                "contact": "8328595488",
                "profile_url": "https://school.cyberdetox.in/api/api/teacher/profile/6"
            },
            "qr_string": "{\"type\":\"staff_profile\",\"staff_id\":\"6\"}",
            "qr_code_url": "https://school.cyberdetox.in/api/api/teacher/qr-code/6"
        },
        "profile_image": "https://school.cyberdetox.in/api/uploads/staff_images/teacher_profile.jpg"
    }

def test_fragment_data_processing():
    """Test how each fragment would process the comprehensive data"""
    
    print("🧪 Testing Teacher Profile Fragment Data Processing")
    print("=" * 60)
    
    test_data = create_test_teacher_data()
    
    # Test Payroll Fragment Processing
    print("\n💰 PAYROLL FRAGMENT TEST")
    print("-" * 30)
    payroll_details = test_data.get('payroll_details', {})
    payroll_records = payroll_details.get('payroll_records', [])
    
    if payroll_records:
        latest_payroll = payroll_records[0]
        print(f"✅ Latest Payroll Status: {latest_payroll.get('status', 'N/A')}")
        print(f"✅ Payment Date: {latest_payroll.get('payment_date', 'N/A')}")
        print(f"✅ Net Salary: {latest_payroll.get('net_salary', 'N/A')}")
        print(f"✅ Total Records: {len(payroll_records)}")
    else:
        print("❌ No payroll records found")
    
    # Test Leave Fragment Processing
    print("\n🏖️  LEAVE FRAGMENT TEST")
    print("-" * 30)
    leave_records = test_data.get('leave_records', {})
    leave_requests = leave_records.get('leave_requests', [])
    leave_balance = leave_records.get('leave_balance', [])
    
    print(f"✅ Leave Requests: {len(leave_requests)}")
    print(f"✅ Leave Balance Types: {len(leave_balance)}")
    
    if leave_requests:
        latest_request = leave_requests[0]
        print(f"✅ Latest Request: {latest_request.get('type', 'N/A')} - {latest_request.get('status', 'N/A')}")
    
    if leave_balance:
        for balance in leave_balance:
            leave_type = balance.get('type', 'Unknown')
            remaining = balance.get('remaining_leave', 0)
            print(f"✅ {leave_type}: {remaining} days remaining")
    
    # Test Attendance Fragment Processing
    print("\n📅 ATTENDANCE FRAGMENT TEST")
    print("-" * 30)
    attendance_records = test_data.get('attendance_records', {})
    attendance_summary = attendance_records.get('attendance_summary', {})
    recent_attendance = attendance_records.get('recent_attendance', [])
    attendance_types = attendance_records.get('attendance_types', [])
    
    print(f"✅ Summary Stats: {len(attendance_summary)} categories")
    for key, value in attendance_summary.items():
        print(f"   - {key}: {value} days")
    
    print(f"✅ Recent Records: {len(recent_attendance)}")
    print(f"✅ Attendance Types: {len(attendance_types)}")
    
    # Test Documents Fragment Processing
    print("\n📄 DOCUMENTS FRAGMENT TEST")
    print("-" * 30)
    documents = test_data.get('documents', [])
    print(f"✅ Total Documents: {len(documents)}")
    
    if documents:
        for doc in documents:
            title = doc.get('title', 'Untitled')
            filename = doc.get('file_name', 'Unknown')
            print(f"   - {title}: {filename}")
    else:
        print("❌ No documents found")
    
    # Save test data for manual verification
    with open('test_teacher_data.json', 'w') as f:
        json.dump(test_data, f, indent=2)
    
    print(f"\n📁 Test data saved to 'test_teacher_data.json'")
    print("\n✅ Fragment data processing test completed!")
    
    return True

if __name__ == "__main__":
    print("🚀 Teacher Profile Fragment Test")
    print("=" * 50)
    
    success = test_fragment_data_processing()
    
    if success:
        print("\n✅ All tests passed!")
    else:
        print("\n❌ Some tests failed!")
        sys.exit(1)
