# Teacher API Documentation

## Overview

This documentation covers the comprehensive Teacher API endpoints implemented in the Smart School Management System. The API provides authentication, profile management, permissions, and various teacher-related functionalities.

## Base URL
```
http://{domain}/api/
```

## Authentication Requirements

### Required Headers (All Requests)
All API requests must include these headers:

```http
Client-Service: smartschool
Auth-Key: schoolAdmin@
Content-Type: application/json
```

### Authentication Methods
The API supports **hybrid authentication** for protected endpoints:

#### 1. Header-Based Authentication (Recommended for GET requests)
```http
User-ID: {staff_id}
Authorization: {jwt_token}
```

#### 2. JSON Body Authentication (Recommended for POST/PUT requests)
```json
{
  "staff_id": "{staff_id}",
  "jwt_token": "{jwt_token}",
  // ... other parameters
}
```

## API Endpoints

---

## 1. Teacher Authentication APIs

### 1.1 Teacher Login

**Endpoint:** `POST /teacher/login`

**Description:** Authenticates a teacher and returns JWT token and profile information.

**Request Headers:**
```http
Client-Service: smartschool
Auth-Key: schoolAdmin@
Content-Type: application/json
```

**Request Body:**
```json
{
  "email": "teacher@example.com",
  "password": "password123",
  "deviceToken": "optional_device_token"
}
```

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Login successful",
  "data": {
    "staff_id": 6,
    "jwt_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
    "token_expires": "2025-12-31 23:59:59",
    "profile": {
      "id": 6,
      "employee_id": "EMP001",
      "name": "John Doe",
      "email": "teacher@example.com",
      "designation": "Teacher",
      "department": "Mathematics",
      "is_active": 1
    }
  }
}
```

**Error Response (401):**
```json
{
  "status": 0,
  "message": "Invalid email or password"
}
```

**Error Response (400):**
```json
{
  "status": 400,
  "message": "Email and password are required"
}
```

### 1.2 Teacher Logout

**Endpoint:** `POST /teacher/logout`

**Description:** Logs out the teacher and invalidates the JWT token.

**Authentication:** Required (Hybrid)

**Request Body:**
```json
{
  "staff_id": 6,
  "jwt_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
  "deviceToken": "optional_device_token"
}
```

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Logout successful"
}
```

### 1.3 Get Teacher Profile

**Endpoint:** `GET /teacher/profile`

**Description:** Retrieves the authenticated teacher's profile information.

**Authentication:** Required (Hybrid)

**Request Headers (Alternative 1):**
```http
Client-Service: smartschool
Auth-Key: schoolAdmin@
User-ID: 6
Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...
```

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Profile retrieved successfully",
  "data": {
    "id": 6,
    "employee_id": "EMP001",
    "name": "John Doe",
    "surname": "Smith",
    "email": "teacher@example.com",
    "designation": "Teacher",
    "department": "Mathematics",
    "phone": "+1234567890",
    "address": "123 Main St",
    "is_active": 1,
    "created_at": "2024-01-01 00:00:00"
  }
}
```

### 1.4 Update Teacher Profile

**Endpoint:** `PUT /teacher/profile`

**Description:** Updates the authenticated teacher's profile information.

**Authentication:** Required

**Request Body:**
```json
{
  "staff_id": 6,
  "jwt_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
  "name": "John",
  "surname": "Doe",
  "phone": "+1234567890",
  "address": "456 New Street"
}
```

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Profile updated successfully",
  "data": {
    "updated_fields": ["name", "surname", "phone", "address"]
  }
}
```

### 1.5 Change Password

**Endpoint:** `PUT /teacher/change-password`

**Description:** Changes the authenticated teacher's password.

**Authentication:** Required

**Request Body:**
```json
{
  "staff_id": 6,
  "jwt_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
  "current_password": "oldpassword123",
  "new_password": "newpassword456"
}
```

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Password changed successfully"
}
```

**Error Response (400):**
```json
{
  "status": 400,
  "message": "Current password and new password are required"
}
```

### 1.6 Refresh JWT Token

**Endpoint:** `POST /teacher/refresh-token`

**Description:** Refreshes an existing JWT token to extend its validity.

**Request Body:**
```json
{
  "jwt_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9..."
}
```

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Token refreshed successfully",
  "data": {
    "jwt_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
    "token_expires": "2025-12-31 23:59:59"
  }
}
```

### 1.7 Validate JWT Token

**Endpoint:** `POST /teacher/validate-token`

**Description:** Validates if a JWT token is still valid and active.

**Request Body:**
```json
{
  "jwt_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9..."
}
```

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Token is valid",
  "data": {
    "is_valid": true,
    "staff_id": 6,
    "expires_at": "2025-12-31 23:59:59"
  }
}
```

---

## 2. Teacher Permissions APIs

### 2.1 Get Teacher Permissions

**Endpoint:** `GET /teacher/permissions`

**Description:** Retrieves all permissions assigned to the authenticated teacher.

**Authentication:** Required (Hybrid)

**Request Headers:**
```http
Client-Service: smartschool
Auth-Key: schoolAdmin@
User-ID: 6
Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...
```

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Permissions retrieved successfully",
  "data": {
    "role": {
      "id": 2,
      "name": "Teacher",
      "slug": "teacher",
      "is_superadmin": false
    },
    "permissions": {
      "student_information": {
        "group_id": 1,
        "group_name": "Student Information",
        "permissions": {
          "student_information": {
            "permission_id": 1,
            "permission_name": "Student Information",
            "can_view": true,
            "can_add": false,
            "can_edit": true,
            "can_delete": false
          }
        }
      },
      "attendance": {
        "group_id": 2,
        "group_name": "Attendance",
        "permissions": {
          "attendance": {
            "permission_id": 2,
            "permission_name": "Attendance",
            "can_view": true,
            "can_add": true,
            "can_edit": true,
            "can_delete": false
          }
        }
      }
    },
    "summary": {
      "total_permission_groups": 2,
      "total_permissions": 2,
      "active_permissions": 2
    }
  }
}
```

### 2.2 Check Specific Permission

**Endpoint:** `POST /teacher/check-permission`

**Description:** Checks if the authenticated teacher has a specific permission.

**Authentication:** Required

**Request Body:**
```json
{
  "staff_id": 6,
  "jwt_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
  "category": "student_information",
  "permission": "can_view"
}
```

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Permission check completed",
  "data": {
    "category": "student_information",
    "permission": "can_view",
    "has_permission": true,
    "role": {
      "id": 2,
      "name": "Teacher",
      "is_superadmin": false
    }
  }
}
```

### 2.3 Bulk Permission Check

**Endpoint:** `POST /teacher/bulk-permission-check`

**Description:** Checks multiple permissions at once for the authenticated teacher.

**Authentication:** Required

**Request Body:**
```json
{
  "staff_id": 6,
  "jwt_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
  "permissions": [
    {
      "category": "student_information",
      "permission": "can_view",
      "identifier": "student_view"
    },
    {
      "category": "attendance",
      "permission": "can_add",
      "identifier": "attendance_add"
    },
    {
      "category": "examinations",
      "permission": "can_edit",
      "identifier": "exam_edit"
    }
  ]
}
```

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Bulk permission check completed",
  "data": {
    "permission_checks": [
      {
        "category": "student_information",
        "permission": "can_view",
        "has_permission": true,
        "identifier": "student_view"
      },
      {
        "category": "attendance",
        "permission": "can_add",
        "has_permission": true,
        "identifier": "attendance_add"
      },
      {
        "category": "examinations",
        "permission": "can_edit",
        "has_permission": false,
        "identifier": "exam_edit"
      }
    ],
    "total_checks": 3,
    "granted_count": 2
  }
}
```

### 2.4 Get Teacher Role Information

**Endpoint:** `GET /teacher/role`

**Description:** Retrieves the role information for the authenticated teacher.

**Authentication:** Required (Hybrid)

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Role information retrieved successfully",
  "data": {
    "role": {
      "id": 2,
      "name": "Teacher",
      "slug": "teacher",
      "is_superadmin": false
    },
    "staff_info": {
      "id": 6,
      "employee_id": "EMP001",
      "name": "John Doe",
      "designation": "Mathematics Teacher",
      "department": "Mathematics"
    }
  }
}
```

### 2.5 Get Permission Groups

**Endpoint:** `GET /teacher/permission-groups`

**Description:** Retrieves all permission groups with access summary for the teacher.

**Authentication:** Required (Hybrid)

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Permission groups retrieved successfully",
  "data": {
    "permission_groups": [
      {
        "group_id": 1,
        "group_name": "Student Information",
        "group_code": "student_information",
        "total_permissions": 4,
        "active_permissions": 2,
        "access_level": "granted"
      },
      {
        "group_id": 2,
        "group_name": "Attendance",
        "group_code": "attendance",
        "total_permissions": 4,
        "active_permissions": 3,
        "access_level": "granted"
      }
    ],
    "total_groups": 2
  }
}
```

### 2.6 Get Group Permissions Detail

**Endpoint:** `POST /teacher/group-permissions`

**Description:** Retrieves detailed permissions for a specific permission group.

**Authentication:** Required

**Request Body:**
```json
{
  "staff_id": 6,
  "jwt_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
  "group_code": "student_information"
}
```

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Group permissions retrieved successfully",
  "data": {
    "group_info": {
      "group_id": 1,
      "group_name": "Student Information",
      "group_code": "student_information"
    },
    "permissions": [
      {
        "permission_id": 1,
        "permission_name": "Student Information",
        "permission_code": "student_information",
        "can_view": true,
        "can_add": false,
        "can_edit": true,
        "can_delete": false,
        "has_any_access": true
      }
    ],
    "total_permissions": 1
  }
}
```

---

## 3. Teacher Menu and Navigation APIs

### 3.1 Get Teacher Menu Items

**Endpoint:** `GET /teacher/menu`

**Description:** Retrieves all accessible menu items for the authenticated teacher.

**Authentication:** Required (Hybrid)

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Menu items retrieved successfully",
  "data": {
    "role": {
      "id": 2,
      "name": "Teacher",
      "slug": "teacher",
      "is_superadmin": false
    },
    "menus": [
      {
        "id": 1,
        "menu": "Student Information",
        "icon": "fa fa-users",
        "lang_key": "student_information",
        "level": 1,
        "submenus": [
          {
            "id": 1,
            "menu": "Student Details",
            "key": "student_details",
            "url": "student/search",
            "activate_controller": "student",
            "activate_methods": "search,view,profile"
          }
        ]
      }
    ],
    "total_menus": 1
  }
}
```

### 3.2 Get Sidebar Menu Structure

**Endpoint:** `GET /teacher/sidebar-menu`

**Description:** Retrieves formatted sidebar menu structure for UI rendering.

**Authentication:** Required (Hybrid)

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Sidebar menu structure retrieved successfully",
  "data": {
    "sidebar_menu": [
      {
        "id": 1,
        "title": "Student Information",
        "icon": "fa fa-users",
        "key": "student_information",
        "level": 1,
        "has_submenu": true,
        "submenu_count": 2,
        "children": [
          {
            "id": 1,
            "title": "Student Details",
            "key": "student_details",
            "url": "student/search",
            "controller": "student",
            "methods": ["search", "view", "profile"]
          },
          {
            "id": 2,
            "title": "Student Admission",
            "key": "student_admission",
            "url": "student/create",
            "controller": "student",
            "methods": ["create", "edit"]
          }
        ]
      }
    ],
    "total_main_menus": 1,
    "total_submenus": 2
  }
}
```

### 3.3 Get Navigation Breadcrumb

**Endpoint:** `POST /teacher/breadcrumb`

**Description:** Retrieves breadcrumb navigation information for a specific controller and method.

**Authentication:** Required

**Request Body:**
```json
{
  "staff_id": 6,
  "jwt_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
  "controller": "student",
  "method": "search"
}
```

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Breadcrumb information retrieved",
  "data": {
    "breadcrumb": {
      "main_menu": {
        "id": 1,
        "title": "Student Information",
        "icon": "fa fa-users"
      },
      "submenu": {
        "id": 1,
        "title": "Student Details",
        "url": "student/search"
      },
      "current": {
        "controller": "student",
        "method": "search"
      }
    },
    "found": true
  }
}
```

---

## 4. Teacher Dashboard and System APIs

### 4.1 Get Teacher Dashboard Data

**Endpoint:** `GET /teacher/dashboard`

**Description:** Retrieves dashboard data and statistics for the authenticated teacher.

**Authentication:** Required (Hybrid)

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Dashboard data retrieved successfully",
  "data": {
    "summary": {
      "total_students": 150,
      "total_classes": 5,
      "pending_homework": 12,
      "upcoming_exams": 3
    },
    "recent_activities": [
      {
        "type": "homework_submitted",
        "message": "New homework submitted by John Smith",
        "timestamp": "2024-01-15 10:30:00"
      }
    ],
    "quick_stats": {
      "attendance_today": 95.5,
      "assignments_pending": 8,
      "messages_unread": 3
    }
  }
}
```

### 4.2 Get Dashboard Summary

**Endpoint:** `GET /teacher/dashboard-summary`

**Description:** Retrieves comprehensive dashboard summary with permissions and access statistics.

**Authentication:** Required (Hybrid)

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Dashboard summary retrieved successfully",
  "data": {
    "role_info": {
      "id": 2,
      "name": "Teacher",
      "is_superadmin": false
    },
    "access_summary": {
      "total_permission_groups": 8,
      "total_permissions": 32,
      "active_permissions": 24,
      "permission_percentage": 75.0,
      "accessible_modules": 6,
      "main_menus": 4,
      "submenus": 12
    },
    "quick_stats": {
      "has_student_access": true,
      "has_attendance_access": true,
      "has_exam_access": true,
      "has_homework_access": true,
      "has_report_access": false
    }
  }
}
```

### 4.3 Get System Settings

**Endpoint:** `GET /teacher/settings`

**Description:** Retrieves system settings relevant to teachers.

**Authentication:** Required (Hybrid)

**Success Response (200):**
```json
{
  "status": 1,
  "message": "System settings retrieved successfully",
  "data": {
    "school_name": "Smart School",
    "school_code": "SS001",
    "session_id": "2024-25",
    "currency_symbol": "$",
    "currency": "USD",
    "date_format": "d-m-Y",
    "time_format": "H:i",
    "timezone": "America/New_York",
    "language": "english",
    "is_rtl": "0",
    "theme": "default",
    "start_week": "Monday",
    "student_login": "1",
    "parent_login": "1"
  }
}
```

### 4.4 Get Teacher Features Access

**Endpoint:** `GET /teacher/features`

**Description:** Retrieves access status for common teacher features.

**Authentication:** Required (Hybrid)

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Teacher features access retrieved successfully",
  "data": {
    "features": [
      {
        "feature_code": "student_management",
        "feature_name": "Student Management",
        "has_access": true,
        "granted_permissions": ["view", "edit"],
        "total_permissions": 2
      },
      {
        "feature_code": "attendance",
        "feature_name": "Attendance Management",
        "has_access": true,
        "granted_permissions": ["view", "add"],
        "total_permissions": 2
      },
      {
        "feature_code": "examinations",
        "feature_name": "Examinations",
        "has_access": false,
        "granted_permissions": [],
        "total_permissions": 2
      }
    ],
    "total_features": 3,
    "accessible_features": 2
  }
}
```

---

## 5. Staff Management APIs

### 5.1 Get Staff Details by ID

**Endpoint:** `GET /teacher/staff/{id}`

**Description:** Retrieves detailed information about a specific staff member.

**Authentication:** Required (Hybrid)

**URL Parameters:**
- `id` (required): Staff member ID

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Staff details retrieved successfully",
  "data": {
    "id": 6,
    "employee_id": "EMP001",
    "name": "John",
    "surname": "Doe",
    "email": "john.doe@school.com",
    "designation": "Mathematics Teacher",
    "department": "Mathematics",
    "phone": "+1234567890",
    "address": "123 Main Street",
    "is_active": 1,
    "created_at": "2024-01-01 00:00:00"
  }
}
```

**Error Response (404):**
```json
{
  "status": 0,
  "message": "Staff not found"
}
```

### 5.2 Search Staff Members

**Endpoint:** `GET /teacher/staff-search`

**Description:** Searches for staff members with optional filters and pagination.

**Authentication:** Required (Hybrid)

**Query Parameters:**
- `search` (optional): Search term for name, email, or employee ID
- `role_id` (optional): Filter by role ID
- `is_active` (optional): Filter by active status (default: 1)
- `limit` (optional): Number of results per page (default: 20, max: 100)
- `offset` (optional): Offset for pagination (default: 0)

**Example Request:**
```
GET /teacher/staff-search?search=john&role_id=2&limit=10&offset=0
```

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Staff search completed successfully",
  "data": {
    "staff": [
      {
        "id": 6,
        "employee_id": "EMP001",
        "name": "John Doe",
        "email": "john.doe@school.com",
        "designation": "Teacher",
        "department": "Mathematics",
        "is_active": 1
      }
    ],
    "pagination": {
      "total_records": 1,
      "current_page": 1,
      "per_page": 10,
      "total_pages": 1,
      "has_next": false,
      "has_previous": false
    },
    "search_params": {
      "search_term": "john",
      "role_id": "2",
      "is_active": "1"
    }
  }
}
```

### 5.3 Get Staff by Role

**Endpoint:** `GET /teacher/staff-by-role/{role_id}`

**Description:** Retrieves all staff members assigned to a specific role.

**Authentication:** Required (Hybrid)

**URL Parameters:**
- `role_id` (required): Role ID to filter by

**Query Parameters:**
- `is_active` (optional): Filter by active status (default: 1)

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Staff list retrieved successfully",
  "data": {
    "role_id": "2",
    "staff_count": 5,
    "staff": [
      {
        "id": 6,
        "employee_id": "EMP001",
        "name": "John Doe",
        "email": "john.doe@school.com",
        "designation": "Teacher",
        "department": "Mathematics"
      }
    ]
  }
}
```

### 5.4 Get Staff by Employee ID

**Endpoint:** `GET /teacher/staff-by-employee-id/{employee_id}`

**Description:** Retrieves staff member information by their employee ID.

**Authentication:** Required (Hybrid)

**URL Parameters:**
- `employee_id` (required): Employee ID to search for

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Staff details retrieved successfully",
  "data": {
    "id": 6,
    "employee_id": "EMP001",
    "name": "John Doe",
    "email": "john.doe@school.com",
    "designation": "Teacher",
    "department": "Mathematics",
    "is_active": 1
  }
}
```

---

## 6. Utility and Testing APIs

### 6.1 API Test Endpoint

**Endpoint:** `GET /teacher/test`

**Description:** Tests if the Teacher API controller is working properly.

**Authentication:** Not required

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Teacher Auth Controller is working",
  "timestamp": "2024-01-15 10:30:00",
  "database_connected": true,
  "models_loaded": {
    "teacher_auth_model": true,
    "staff_model": true,
    "setting_model": true
  }
}
```

### 6.2 Simple Login (Testing)

**Endpoint:** `POST /teacher/simple-login`

**Description:** Simplified login endpoint for testing purposes (without JWT).

**Request Body:**
```json
{
  "email": "teacher@example.com",
  "password": "password123"
}
```

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Login successful",
  "staff_id": 6,
  "name": "John Doe",
  "email": "teacher@example.com"
}
```

### 6.3 Check Credentials (Testing)

**Endpoint:** `POST /teacher/check-credentials`

**Description:** Checks if teacher credentials exist in the database (for testing).

**Request Body:**
```json
{
  "email": "teacher@example.com",
  "password": "password123"
}
```

**Success Response (200):**
```json
{
  "status": 1,
  "message": "Teacher record found",
  "data": {
    "staff_id": 6,
    "email": "teacher@example.com",
    "name": "John Doe",
    "is_active": 1,
    "designation": "Teacher",
    "department": "Mathematics",
    "password_provided": true,
    "password_match": true,
    "stored_password_length": 60
  }
}
```

---

## HTTP Status Codes

The API uses standard HTTP status codes:

- **200 OK**: Request successful
- **400 Bad Request**: Invalid request parameters or missing required fields
- **401 Unauthorized**: Authentication failed or missing credentials
- **404 Not Found**: Requested resource not found
- **500 Internal Server Error**: Server error occurred

## Error Handling

All error responses follow this format:

```json
{
  "status": 0,
  "message": "Error description",
  "error_code": "OPTIONAL_ERROR_CODE"
}
```

### Common Error Messages

- `"Unauthorized access. Invalid client credentials."` - Invalid Client-Service or Auth-Key headers
- `"Email and password are required."` - Missing required login parameters
- `"Invalid email or password."` - Authentication failed
- `"JWT token is required."` - Missing JWT token for authenticated endpoint
- `"Token expired or invalid."` - JWT token is no longer valid
- `"Bad request."` - Wrong HTTP method used for endpoint

## Usage Notes

### Authentication Flow
1. Use `/teacher/login` to authenticate and receive JWT token
2. Include JWT token in subsequent requests using hybrid authentication
3. Use `/teacher/refresh-token` to extend token validity
4. Use `/teacher/logout` to invalidate token

### Best Practices
- Always include required headers (`Client-Service`, `Auth-Key`)
- Use HTTPS in production environments
- Store JWT tokens securely on client side
- Implement proper error handling for all API calls
- Use pagination for list endpoints to improve performance
- Validate permissions before allowing UI actions

### Rate Limiting
- No explicit rate limiting is currently implemented
- Consider implementing rate limiting in production environments

### Dependencies
- PHP 7.4+ with CodeIgniter 3.x framework
- MySQL database with proper teacher/staff tables
- JWT library for token management
- Custom authentication models and libraries

This documentation covers all the teacher-related API endpoints available in the system. For additional support or questions, please refer to the implementation files or contact the development team.

