package com.qdocs.ssre241123.utils;

public class Constants {
    public static final Boolean isDemoModeOn = false;
    public static final Boolean askUrlFromUser = false;
    public static final Boolean checkMaintenanceMode = false; // Set to false to skip maintenance check (backend has error)
    public static final String clientService = "smartschool";
    public static final String authKey = "schoolAdmin@";
    public static final String appKey = "schoolAdmin@1234";
    public static final String contentType = "application/json";
    public static final String contentTypes = "application/x-www-form-urlencoded";
    public static final String domain = "https://school.cyberdetox.in";
    public static final String loginUrl = "auth/login";
    
    public static final String teacherLoginUrl = "teacher/login";
    public static final String teacherLogoutUrl = "teacher/logout";
    public static final String teacherProfileUrl = "teacher/profile";
    public static final String teacherMenuUrl = "teacher/menu";
    public static final String teacherSessionsWithClassesSectionsUrl = "teacher/sessions-with-classes-sections";
    public static final String teacherStudentsUrl = "teacher/students";

    // Student Categories API endpoints
    public static final String teacherStudentCategoriesUrl = "teacher/student-categories";
    public static final String teacherStudentCategoryGetUrl = "teacher/student-category/get";
    public static final String teacherStudentCategoryCreateUrl = "teacher/student-category/create";
    public static final String teacherStudentCategoryUpdateUrl = "teacher/student-category/update";
    public static final String teacherStudentCategoryDeleteUrl = "teacher/student-category/delete";

    // Student House API endpoints
    public static final String studentHouseListUrl = "student-house/list";
    public static final String studentHouseGetUrl = "student-house/get";
    public static final String studentHouseCreateUrl = "student-house/create";
    public static final String studentHouseUpdateUrl = "student-house/update";
    public static final String studentHouseDeleteUrl = "student-house/delete";

    // Disable Reason API endpoints
    public static final String disableReasonListUrl = "disable-reason/list";
    public static final String disableReasonGetUrl = "disable-reason/get";
    public static final String disableReasonCreateUrl = "disable-reason/create";
    public static final String disableReasonUpdateUrl = "disable-reason/update";
    public static final String disableReasonDeleteUrl = "disable-reason/delete";

    // Parent Login Detail Report API endpoints
    public static final String parentLoginDetailReportFilterUrl = "parent-login-detail-report/filter";
    public static final String parentLoginDetailReportListUrl = "parent-login-detail-report/list";

    // Student Login Detail Report API endpoints
    public static final String loginDetailReportFilterUrl = "login-detail-report/filter";
    public static final String loginDetailReportListUrl = "login-detail-report/list";

    // Guardian Report API endpoints
    public static final String guardianReportFilterUrl = "guardian-report/filter";
    public static final String guardianReportListUrl = "guardian-report/list";

    // Student Profile Report API endpoints
    public static final String studentProfileReportFilterUrl = "student-profile-report/filter";
    public static final String studentProfileReportListUrl = "student-profile-report/list";

    // Admission Report API endpoints
    public static final String admissionReportFilterUrl = "admission-report/filter";
    public static final String admissionReportListUrl = "admission-report/list";

    // Due Fee Report API endpoints
    public static final String dueFeeReportFilterUrl = "due-fees-report/filter";
    public static final String dueFeeReportListUrl = "due-fees-report/list";

    // Year Report Due Fee API endpoints (Balance Fees Statement)
    public static final String yearReportDueFeeFilterUrl = "year-report-due-fees/filter";
    public static final String yearReportDueFeeListUrl = "year-report-due-fees/list";

    // Daily Collection Report API endpoints
    public static final String dailyCollectionReportFilterUrl = "daily-collection-report/filter";

    // Session Fee Structure API endpoints
    public static final String sessionFeeStructureFilterUrl = "session-fee-structure/filter";
    public static final String sessionFeeStructureListUrl = "session-fee-structure/list";

    // Type Wise Balance Report API endpoints
    public static final String typeWiseBalanceReportFilterUrl = "type-wise-balance-report/filter";

    // Fee Collection Report Column Wise API endpoints
    public static final String feeCollectionReportColumnWiseFilterUrl = "fee-collection-report-column-wise/filter";

    // Fee Collection Filters API endpoints (Hierarchical)
    public static final String feeCollectionFiltersGetUrl = "fee-collection-filters/get";
    public static final String feeCollectionFiltersGetHierarchyUrl = "fee-collection-filters/get-hierarchy";

    // Finance Reports API endpoints
    public static final String totalBalanceFeesReportFilterUrl = "total-balance-fees-report/filter";
    public static final String totalFeeCollectionReportFilterUrl = "total-fee-collection-report/filter";
    public static final String feesCollectionReportFilterUrl = "fees-collection-report/filter";
    public static final String otherFeesCollectionReportFilterUrl = "other-fees-collection-report/filter";
    public static final String otherFeeAndCollectionFeeCombinedFilterUrl = "other-fee-and-collection-fee-combined/filter";
    public static final String combinedCollectionReportFilterUrl = "combined-collection-report/filter";
    public static final String balanceFeesReportFilterUrl = "balance-fees-report/filter";
    public static final String balanceFeesReportWithRemarkFilterUrl = "due-fees-remark-report/filter";
    public static final String feesStatementFilterUrl = "fees-statement/filter";
    public static final String onlineFeesReportFilterUrl = "online-fees-report/filter";

    // Total Student Academic Report API endpoints
    public static final String totalStudentAcademicReportFilterUrl = "total-student-academic-report/filter";
    public static final String totalStudentAcademicReportListUrl = "total-student-academic-report/list";

    // Staff Attendance Report API endpoints (OLD - replaced with monthly report)
    public static final String staffAttendanceReportFilterUrl = "staff-attendance-report/filter";
    public static final String staffAttendanceReportListUrl = "staff-attendance-report/list";
    public static final String staffAttendanceYearsListUrl = "staff-attendance-years/list";
    
    // Monthly Staff Attendance Report API endpoints (NEW)
    public static final String monthlyStaffAttendanceReportUrl = "monthly-staff-attendance/report";
    public static final String monthlyStaffAttendanceAvailablePeriodsUrl = "monthly-staff-attendance/available-periods";

    // Class Attendance Years API endpoint
    public static final String classAttendanceYearsListUrl = "class-attendance-years/list";

    // Collection Report API endpoints
    public static final String collectionReportFilterUrl = "collection-report/filter";
    public static final String collectionReportListUrl = "collection-report/list";
    public static final String onlineFeesReportListUrl = "online-fees-report/list";

    // Online Admission Fee Report API endpoints
    public static final String onlineAdmissionReportFilterUrl = "online-admission-report/filter";
    public static final String onlineAdmissionReportListUrl = "online-admission-report/list";

    // Other Collection Report API endpoints
    public static final String otherCollectionReportFilterUrl = "other-collection-report/filter";
    public static final String otherCollectionReportListUrl = "other-collection-report/list";

    // Income Report API endpoints
    public static final String incomeReportFilterUrl = "income-report/filter";
    public static final String incomeReportListUrl = "income-report/list";

    // Income Group Report API endpoints
    public static final String incomeGroupReportFilterUrl = "income-group-report/filter";
    public static final String incomeGroupReportListUrl = "income-group-report/list";

    // Expense Report API endpoints
    public static final String expenseReportFilterUrl = "expense-report/filter";
    public static final String expenseReportListUrl = "expense-report/list";

    // Payroll Report API endpoints
    public static final String payrollReportFilterUrl = "payroll-report/filter";
    public static final String payrollReportListUrl = "payroll-report/list";

    // Expense Group Report API endpoints
    public static final String expenseGroupReportFilterUrl = "expense-group-report/filter";
    public static final String expenseGroupReportListUrl = "expense-group-report/list";

    // List APIs for dropdowns (Note: base URL already includes /api/)
    public static final String incomeHeadListUrl = "income-head-list/list";
    public static final String expenseHeadListUrl = "expense-head-list/list";
    public static final String rolesListUrl = "roles-list/list";

    // User Log API endpoints
    public static final String userLogFilterUrl = "user-log/filter";
    public static final String userLogListUrl = "user-log/list";

    // Alumni Report API endpoints
    public static final String alumniReportFilterUrl = "alumni-report/filter";
    public static final String alumniReportListUrl = "alumni-report/list";

    // Student Academic Report API endpoints
    public static final String studentAcademicReportFilterUrl = "student-academic-report/filter";
    public static final String studentAcademicReportListUrl = "student-academic-report/list";

    // Class Attendance Report API endpoints
    public static final String classAttendanceReportFilterUrl = "class-attendance-report/filter";
    public static final String classAttendanceReportListUrl = "class-attendance-report/list";

    // Daily Attendance Report API endpoints
    public static final String dailyAttendanceReportFilterUrl = "daily-attendance-report/filter";
    public static final String dailyAttendanceReportListUrl = "daily-attendance-report/list";

    // Biometric Attendance Log Report API endpoints
    public static final String biometricAttlogReportFilterUrl = "biometric-attlog-report/filter";
    public static final String biometricAttlogReportListUrl = "biometric-attlog-report/list";

    // Report By Name API endpoints
    public static final String reportByNameFilterUrl = "report-by-name/filter";
    public static final String reportByNameListUrl = "report-by-name/list";

    public static final String getStudentProfileUrl = "webservice/getStudentProfile";
    public static final String getHomeworkUrl = "webservice/getHomework";
    public static final String uploadHomeworkUrl = "webservice/addaa";
    public static final String getstudentcurrentlanguageUrl = "webservice/getstudentcurrentlanguage";
    public static final String get_currency_listUrl = "webservice/get_currency_list";
    public static final String updatestudentlanguage = "webservice/updatestudentlanguage";
    public static final String updatestudentcurrency = "webservice/updatestudentcurrency";
    public static final String getstudentsubjectUrl = "webservice/getstudentsubject";
    public static final String uploadDocumentUrl = "webservice/uploadDocument";
    public static final String getAppliedDiscountsUrl = "webservice/getAppliedDiscounts";
    public static final String getFeesUrl = "webservice/fees";
    public static final String getProcessingfeesUrl = "webservice/getProcessingfees";
    public static final String lock_student_panelUrl = "webservice/lock_student_panel";
    public static final String getMaintenanceModeStatusUrl = "webservice/getMaintenanceModeStatus";
    public static final String getDownloadsLinksByIdUrl = "webservice/getDownloadsLinksById";
    public static final String checkStudentStatusUrl = "webservice/checkStudentStatus";
    public static final String getStudentTimelineStatusUrl = "webservice/getStudentTimelineStatus";
    public static final String getOfflineBankPaymentStatusUrl = "webservice/getOfflineBankPaymentStatus";
    public static final String getfeesdiscountstatusStatusUrl = "webservice/getfeesdiscountstatus";
    public static final String getOnlineCourseSettingsUrl = "webservice/getOnlineCourseSettings";
    public static final String getOfflineBankPaymentInstructionUrl = "webservice/getOfflineBankPaymentInstruction";
    public static final String getgmeetsettingsUrl = "webservice/getgmeetsettings";
    public static final String getzoomsettingsUrl = "webservice/getzoomsettings";
    public static final String getClassScheduleUrl = "webservice/class_schedule";
    public static final String coursecurriculumUrl = "webservice/coursecurriculum";
    public static final String getCourseReviewsUrl = "webservice/getCourseReviews";

    public static final String courseperformanceUrl = "webservice/courseperformance";
    public static final String getlessonplanUrl = "webservice/getlessonplan";
    public static final String getExamListUrl = "webservice/getExamList";
    public static final String cbseexamresultUrl = "webservice/cbseexamresult";
    public static final String cbseexamtimetableUrl = "webservice/cbseexamtimetable";
    public static final String getExamScheduleListUrl = "webservice/examSchedule";
    public static final String getOnlineExamQuestionUrl = "webservice/getOnlineExamQuestion";
    public static final String getOnlineCourseQuestionUrl = "webservice/getOnlineCourseQuestion";
    public static final String getquestionbyquizidUrl = "webservice/getquestionbyquizid";
    public static final String saveOnlineExamUrl = "webservice/saveOnlineExam";
    public static final String saveOnlineCourseExamUrl = "webservice/saveOnlineCourseExam";
    public static final String saveanswerUrl = "webservice/saveanswer";
    public static final String submitquizUrl = "webservice/submitquiz";
    public static final String quizresultUrl = "webservice/quizresult";
    public static final String resetquizUrl = "webservice/resetquiz";
    public static final String coursedetailUrl = "webservice/coursedetail";
    public static final String getExamResultListUrl = "webservice/getExamResultList";
    public static final String getExamScheduleDetailsUrl = "webservice/getExamSchedule";
    public static final String getExamResultUrl = "webservice/getExamResult";
    public static final String getSubjectsLessonsUrl = "webservice/getSubjectsLessons";
    public static final String getNotificationsUrl = "webservice/getNotifications";
    public static final String getsyllabussubjectsUrl = "webservice/getsyllabussubjects";
    public static final String getSubjectListUrl = "webservice/getSubjectList";
    public static final String getSubjectTimetableUrl = "webservice/getSubjectTimetable";
    public static final String getTeacherListUrl = "webservice/getTeachersList";
    public static final String getTeacherSubjectUrl = "webservice/getTeacherSubject";
    public static final String addStaffRatingUrl = "webservice/addStaffRating";
    public static final String getLibraryBookListUrl = "webservice/getLibraryBooks";
    public static final String getLibraryBookIssuedListUrl = "webservice/getLibraryBookIssued";
    public static final String getTransportRouteListUrl = "webservice/gettransportroutes";
    public static final String getTransportVehicleDetailsUrl = "webservice/getTransportVehicleDetails";
    public static final String getHostelListUrl = "webservice/getHostelList";
    public static final String getHostelDetailUrl = "webservice/getHostelDetails";
    public static final String getDownloadsLinksUrl = "webservice/getDownloadsLinks";
    public static final String getVideoTutorialUrl = "webservice/getVideoTutorial";
    public static final String getAttendanceUrl = "webservice/getAttendenceRecords";
    public static final String forgotPasswordUrl = "webservice/forgot_password";
    public static final String getSelectedFeesPayUrl = "payment/getSelectedFeesPay";
    public static final String logoutUrl = "webservice/logout";
    public static final String paymentGatewayUrl = "payment/index/";
    public static final String coursepaymentGatewayUrl = "course_payment/Course_payment/payment/";
    public static final String getDashboardUrl = "webservice/dashboard";
    public static final String getStudentCurrencyUrl = "webservice/getStudentCurrency";
    public static final String getDocumentUrl = "webservice/getDocument";
    public static final String getdailyassignmentUrl = "webservice/getdailyassignment";
    public static final String addeditdailyassignmentUrl = "webservice/addeditdailyassignment";
    public static final String saveCourseAssignmentUrl = "webservice/saveCourseAssignment";
    public static final String deletedailyassignmentUrl = "webservice/deletedailyassignment";
    public static final String getTimelineUrl = "webservice/getTimeline";
    public static final String addedittimelineUrl = "webservice/addedittimeline";
    public static final String addCourseRatingandReviewUrl = "webservice/addCourseRatingandReview";
    public static final String createTaskUrl = "webservice/addTask";
    public static final String deleteTaskUrl = "webservice/deleteTask";
    public static final String markTaskUrl = "webservice/updateTask";
    public static final String getTaskUrl = "webservice/getTask";
    public static final String getSubmitedAssignmentDetailsUrl = "webservice/getSubmitedAssignmentDetails";
    public static final String getBalanceFeeUrl = "webservice/getBalanceFee";
    public static final String paymentrequestUrl = "payment/paymentrequest";
    public static final String getELearningUrl = "webservice/getELearningModuleStatus";
    public static final String getCommunicateUrl = "webservice/getCommunicateModuleStatus";
    public static final String getAcademicsUrl = "webservice/getAcademicsModuleStatus";
    public static final String getOthersUrl = "webservice/getOthersModuleStatus";
    public static final String getOnlineExamUrl = "webservice/getOnlineExam";
    public static final String courselistUrl = "webservice/courselist";
    public static final String liveclassesUrl = "webservice/liveclasses";
    public static final String gmeetclassesUrl = "webservice/gmeetclasses";
    public static final String getstudentbehaviourUrl = "webservice/getstudentbehaviour";
    public static final String getOfflineBankPayments = "webservice/getOfflineBankPayments";
    public static final String livehistoryUrl = "webservice/livehistory";
    public static final String gmeethistoryUrl = "webservice/gmeethistory";
    public static final String getsyllabusUrl = "webservice/getsyllabus";
    public static final String addforummessageUrl = "webservice/addforummessage";
    public static final String addincidentcommentsUrl = "webservice/addincidentcomments";
    public static final String getforummessageUrl = "webservice/getforummessage";
    public static final String deleteforummessageUrl = "webservice/deleteforummessage";
    public static final String getincidentcommentsUrl = "webservice/getincidentcomments";
    public static final String deleteincidentcommentsUrl = "webservice/deleteincidentcomments";
    public static final String getOnlineExamResultUrl = "webservice/getOnlineExamResult";
    public static final String getApplyLeaveUrl = "webservice/getApplyLeave";
    public static final String getVisitorsUrl = "webservice/getVisitors";
    public static final String addofflinepaymentUrl = "webservice/addofflinepayment";
    public static final String addleaveUrl = "webservice/addleave";
    public static final String updateLeaveUrl = "webservice/updateLeave";
    public static final String deleteLeaveUrl = "webservice/deleteLeave";
    public static final String deletetimelineUrl = "webservice/deletetimeline";
    public static final String markAsCompleteUrl = "webservice/markascomplete";
    public static final String getCourseExamDetailsUrl = "webservice/getCourseExamDetails";
    public static final String getSchoolDetailsUrl = "webservice/getSchoolDetails";
    public static final String parent_getStudentList = "webservice/Parent_GetStudentsList";
    public static final String getExamResultDetailsUrl = "webservice/getExamResultDetails";
    public static final String privacyPolicyUrl = "privacy-policy";
    public static final String onlineAdmissionFilterUrl = "online-admission/filter";
    public static final String onlineAdmissionListUrl = "online-admission/list";
    public static final String onlineAdmissionGetUrl = "online-admission/get/";
    public static final String downloadDirectory = "SmartSchool";
    public static final String defaultSecondaryColour = "#daf6fc";
    public static final String defaultPrimaryColour = "#2e4b5f";
    //SHARED PREFERENCE KEYS
    public static final String primaryColour = "primaryColour";
    public static final String secondaryColour = "secondaryColour";
    public static final String app_ver = "app_ver";
    public static final String appLogo = "appLogo";
    public static final String apiUrl = "apiUrl";
    public static final String imagesUrl = "imagesUrl";
    public static final String classSection = "classSection";
    public static final String currency = "currencySymbol";
    public static final String currency_short_name = "currency_short_name";
    public static final String currency_price = "currency_price";
    public static final String classId = "classId";
    public static final String sectionId = "sectionId";
    public static final String studentId = "studentId";
    public static final String parentsId = "parentsId";
    public static final String admission_no = "admission_no";
    public static final String userId = "userId";
    public static final String permissionStatus = "permissionStatus";
    public static final String userName = "userName";
    public static final String userImage = "userImage";
    public static final String chatuserImage = "chatuserImage";
    public static final String loginType = "role";
    public static final String superadmin_restriction = "superadmin_restriction";
    public static final String student_session_id = "student_session_id";
    public static final String modulesArray = "modulesArray";
    public static final String isLoggegIn = "isLoggegIn";
    public static final String isLock = "isLock";



    // Teacher specific constants
    public static final String teacherId = "teacherId";
    public static final String teacherStaffId = "teacherStaffId";
    public static final String teacherEmployeeId = "teacherEmployeeId";
    public static final String teacherName = "teacherName";
    public static final String teacherSurname = "teacherSurname";
    public static final String teacherEmail = "teacherEmail";
    public static final String teacherContact = "teacherContact";
    public static final String teacherDesignation = "teacherDesignation";
    public static final String teacherDepartment = "teacherDepartment";
    public static final String teacherImage = "teacherImage";
    public static final String teacherToken = "teacherToken";
    public static final String teacherJwtToken = "teacherJwtToken";
    public static final String isTeacherLoggedIn = "isTeacherLoggedIn";
    public static final String showPaymentBtn = "showPaymentBtn";
    public static final String showCoursePaymentBtn = "showPaymentBtn";
    public static final String langCode = "langCode";
    public static final String appDomain = "appDomain";
    public static final String isLocaleSet = "isLocaleSet";
    public static final String currentLocale = "currentLocale";
    public static final String currencycode = "currencycode";
    public static final String parent_live_class = "parent_live_class";
    public static final String zoom_parent_live_class = "zoom_parent_live_class";

    // ===== Missing Report API Endpoints =====
    // Student Information Reports
    public static final String siblingReportFilterUrl = "sibling-report/filter";
    public static final String siblingReportListUrl = "sibling-report/list";
    public static final String boysGirlsRatioReportFilterUrl = "boys-girls-ratio-report/filter";
    public static final String boysGirlsRatioReportListUrl = "boys-girls-ratio-report/list";

    // Examination Reports
    public static final String internalResultReportFilterUrl = "internal-result-report/filter-options";
    public static final String internalResultReportUrl = "internal-result-report/get-report";
    public static final String externalResultReportFilterUrl = "external-result-report/filter-options";
    public static final String externalResultReportUrl = "external-result-report/get-report";
    public static final String onlineExamRankReportFilterUrl = "online-exam-rank-report/filter";
    public static final String onlineExamRankReportListUrl = "online-exam-rank-report/list";
    public static final String onlineExamAttendReportFilterUrl = "online-exam-attend-report/filter";
    public static final String onlineExamAttendReportListUrl = "online-exam-attend-report/list";
    public static final String onlineExamsReportFilterUrl = "online-exams-report/filter";
    public static final String onlineExamsReportListUrl = "online-exams-report/list";
    public static final String rankReportFilterUrl = "rank-report/filter";
    public static final String rankReportListUrl = "rank-report/list";

    // Human Resource Reports
    public static final String staffReportFilterUrl = "staff-report/filter";
    public static final String staffReportListUrl = "staff-report/list";
    public static final String lessonPlanReportFilterUrl = "lesson-plan-report/filter";
    public static final String lessonPlanReportListUrl = "lesson-plan-report/list";
    public static final String teacherSyllabusStatusFilterUrl = "teacher-syllabus-status-report/filter";
    public static final String teacherSyllabusStatusListUrl = "teacher-syllabus-status-report/list";

    // Library Reports
    public static final String studentBookIssueReportFilterUrl = "student-book-issue-report/filter";
    public static final String studentBookIssueReportListUrl = "student-book-issue-report/list";
    public static final String bookDueReportFilterUrl = "book-due-report/filter";
    public static final String bookDueReportListUrl = "book-due-report/list";
    public static final String bookInventoryReportFilterUrl = "book-inventory-report/filter";
    public static final String bookInventoryReportListUrl = "book-inventory-report/list";
    public static final String issueReturnReportFilterUrl = "issue-return-report/filter";
    public static final String issueReturnReportListUrl = "issue-return-report/list";

    // Inventory Reports
    public static final String inventoryStockReportFilterUrl = "inventory-stock-report/filter";
    public static final String inventoryStockReportListUrl = "inventory-stock-report/list";
    public static final String addItemReportFilterUrl = "add-item-report/filter";
    public static final String addItemReportListUrl = "add-item-report/list";
    public static final String issueInventoryReportFilterUrl = "issue-inventory-report/filter";
    public static final String issueInventoryReportListUrl = "issue-inventory-report/list";

    // Homework Reports
    public static final String homeworkReportFilterUrl = "homework-report/filter";
    public static final String homeworkReportListUrl = "homework-report/list";
    public static final String dailyAssignmentReportFilterUrl = "daily-assignment-report/filter";
    public static final String dailyAssignmentReportListUrl = "daily-assignment-report/list";
    public static final String evaluationReportFilterUrl = "evaluation-report/filter";
    public static final String evaluationReportListUrl = "evaluation-report/list";

    // Transport & Hostel Reports
    public static final String studentTransportDetailsFilterUrl = "student-transport-details/filter";
    public static final String studentTransportDetailsListUrl = "student-transport-details/list";
    public static final String studentHostelDetailsFilterUrl = "student-hostel-details/filter";
    public static final String studentHostelDetailsListUrl = "student-hostel-details/list";

    // Finance Reports
    public static final String feeGroupwiseCollectionReportFilterUrl = "feegroupwise-collection-report/filter";
    public static final String feeGroupwiseCollectionReportListUrl = "feegroupwise-collection-report/list";

    // System Reports
    public static final String auditLogFilterUrl = "audit-log/filter";
    public static final String auditLogListUrl = "audit-log/list";

    // ========== CRUD API Endpoints (Academics) ==========
    public static final String classesListUrl = "classes/list";
    public static final String classesCreateUrl = "classes/create";
    public static final String classesUpdateUrl = "classes/update/(:num)";
    public static final String classesDeleteUrl = "classes/delete/(:num)";
    public static final String sectionsListUrl = "sections/list";
    public static final String sectionsCreateUrl = "sections/create";
    public static final String sectionsUpdateUrl = "sections/update/(:num)";
    public static final String sectionsDeleteUrl = "sections/delete/(:num)";
    public static final String subjectsListUrl = "subjects/list";
    public static final String subjectsCreateUrl = "subjects/create";
    public static final String subjectsUpdateUrl = "subjects/update/(:num)";
    public static final String subjectsDeleteUrl = "subjects/delete/(:num)";
    public static final String subjectgroupListUrl = "subjectgroup/list";
    public static final String subjectgroupCreateUrl = "subjectgroup/create";
    public static final String subjectgroupUpdateUrl = "subjectgroup/update/(:num)";
    public static final String subjectgroupDeleteUrl = "subjectgroup/delete/(:num)";
    public static final String departmentListUrl = "department/list";
    public static final String departmentCreateUrl = "department/create";
    public static final String departmentUpdateUrl = "department/update/(:num)";
    public static final String departmentDeleteUrl = "department/delete/(:num)";
    public static final String designationListUrl = "designation/list";
    public static final String designationCreateUrl = "designation/create";
    public static final String designationUpdateUrl = "designation/update/(:num)";
    public static final String designationDeleteUrl = "designation/delete/(:num)";

    // ========== CRUD API Endpoints (Fees) ==========
    public static final String feeGroupListUrl = "fee-groups/list";
    public static final String feeGroupCreateUrl = "fee-groups/create";
    public static final String feeGroupUpdateUrl = "fee-groups/update";
    public static final String feeGroupDeleteUrl = "fee-groups/delete";
    public static final String feeTypeListUrl = "fee-types/list";
    public static final String feeTypeCreateUrl = "fee-types/create";
    public static final String feeTypeUpdateUrl = "fee-types/update";
    public static final String feeTypeDeleteUrl = "fee-types/delete";
    public static final String feeMasterListUrl = "fee-masters/list";
    public static final String feeMasterCreateUrl = "fee-masters/create";
    public static final String feeMasterUpdateUrl = "fee-masters/update";
    public static final String feeMasterDeleteUrl = "fee-masters/delete";

    // ========== CRUD API Endpoints (Income/Expense) ==========
    public static final String incomeListUrl = "income/list";
    public static final String incomeCreateUrl = "income/create";
    public static final String incomeUpdateUrl = "income/update/(:num)";
    public static final String incomeDeleteUrl = "income/delete/(:num)";
    public static final String incomeHeadCrudListUrl = "income-head/list";
    public static final String incomeHeadCreateUrl = "income-head/create";
    public static final String incomeHeadUpdateUrl = "income-head/update/(:num)";
    public static final String incomeHeadDeleteUrl = "income-head/delete/(:num)";
    public static final String incomeSearchUrl = "income-search/search";
    public static final String expenseHeadListUrl2 = "expense-head-list/list";

    // ========== CRUD API Endpoints (Front Office) ==========
    public static final String admissionEnquiryListUrl = "admission-enquiry-api/list";
    public static final String admissionEnquiryCreateUrl = "admission-enquiry-api/create";
    public static final String admissionEnquiryUpdateUrl = "admission-enquiry-api/update/(:num)";
    public static final String admissionEnquiryDeleteUrl = "admission-enquiry-api/delete/(:num)";
    public static final String visitorsListUrl = "visitors-api/list";
    public static final String visitorsCreateUrl = "visitors-api/create";
    public static final String visitorsUpdateUrl = "visitors-api/update/(:num)";
    public static final String visitorsDeleteUrl = "visitors-api/delete/(:num)";
    public static final String phoneCallListUrl = "phone-call/list";
    public static final String phoneCallCreateUrl = "phone-call/create";
    public static final String phoneCallUpdateUrl = "phone-call/update/(:num)";
    public static final String phoneCallDeleteUrl = "phone-call/delete/(:num)";
    public static final String postalDispatchListUrl = "postal-dispatch-api/list";
    public static final String postalDispatchCreateUrl = "postal-dispatch-api/create";
    public static final String postalDispatchUpdateUrl = "postal-dispatch-api/update/(:num)";
    public static final String postalDispatchDeleteUrl = "postal-dispatch-api/delete/(:num)";
    public static final String postalReceiveListUrl = "postal-receive-api/list";
    public static final String postalReceiveCreateUrl = "postal-receive-api/create";
    public static final String postalReceiveUpdateUrl = "postal-receive-api/update/(:num)";
    public static final String postalReceiveDeleteUrl = "postal-receive-api/delete/(:num)";
    public static final String complaintListUrl = "complaint-api/list";
    public static final String complaintCreateUrl = "complaint-api/create";
    public static final String complaintUpdateUrl = "complaint-api/update/(:num)";
    public static final String complaintDeleteUrl = "complaint-api/delete/(:num)";
    public static final String visitorsPurposeListUrl = "visitors-purpose-api/list";
    public static final String visitorsPurposeCreateUrl = "visitors-purpose-api/create";
    public static final String visitorsPurposeUpdateUrl = "visitors-purpose-api/update/(:num)";
    public static final String visitorsPurposeDeleteUrl = "visitors-purpose-api/delete/(:num)";

    // ========== CRUD API Endpoints (Accounting) ==========
    public static final String addAccountListUrl = "add-account-api/list";
    public static final String addAccountCreateUrl = "add-account-api/create";
    public static final String addAccountUpdateUrl = "add-account-api/update/(:num)";
    public static final String addAccountDeleteUrl = "add-account-api/delete/(:num)";
    public static final String accountCategoryGroupListUrl = "account-category-group-api/list";
    public static final String accountCategoryGroupCreateUrl = "account-category-group-api/create";
    public static final String accountCategoryGroupUpdateUrl = "account-category-group-api/update/(:num)";
    public static final String accountCategoryGroupDeleteUrl = "account-category-group-api/delete/(:num)";
    public static final String accountCategoryListUrl = "account-category-api/list";
    public static final String accountCategoryCreateUrl = "account-category-api/create";
    public static final String accountCategoryUpdateUrl = "account-category-api/update/(:num)";
    public static final String accountCategoryDeleteUrl = "account-category-api/delete/(:num)";
    public static final String accountTransactionCreateUrl = "account-transaction-api/create";
    public static final String accountTransactionListUrl = "account-transaction-api/list";
    public static final String accountTransactionReportListUrl = "account-transaction-report-api/list";
    public static final String accountTransactionReportSummaryUrl = "account-transaction-report-api/summary";
    public static final String accountReportGenerateUrl = "account-report-api/generate";
    public static final String feeDiscountApprovalListUrl = "fee-discount-approval-api/list";
    public static final String feeDiscountApprovalApproveUrl = "fee-discount-approval-api/approve/(:num)";
    public static final String feeDiscountApprovalRejectUrl = "fee-discount-approval-api/reject/(:num)";
    public static final String studentReferralListUrl = "student-referral-api/list";
    public static final String studentReferralCreateUrl = "student-referral-api/create";
    public static final String studentReferralUpdateUrl = "student-referral-api/update/(:num)";
    public static final String studentReferralDeleteUrl = "student-referral-api/delete/(:num)";

    // ========== CRUD API Endpoints (TC Generation) ==========
    public static final String tcGenerationListUrl = "tc-generation-api/list";
    public static final String tcGenerationCreateUrl = "tc-generation-api/create";
    public static final String tcGenerationUpdateUrl = "tc-generation-api/update/(:num)";
    public static final String tcGenerationDeleteUrl = "tc-generation-api/delete/(:num)";
    public static final String tcGenerationTemplatesUrl = "tc-generation-api/templates";
    public static final String tcGenerationStudentsUrl = "tc-generation-api/students";

    // ========== CRUD API Endpoints (Behaviour) ==========
    public static final String studentbehaviourListUrl = "studentbehaviour/list";
    public static final String studentbehaviourCreateUrl = "studentbehaviour/create";
    public static final String studentbehaviourUpdateUrl = "studentbehaviour/update";
    public static final String studentbehaviourDeleteUrl = "studentbehaviour/delete";
    public static final String behaviourSettingGetUrl = "behaviour/setting/get";
    public static final String behaviourSettingUpdateUrl = "behaviour/setting/update";

    // ========== CRUD API Endpoints (Online Admission) ==========
    public static final String onlineAdmissionCrudGetUrl = "online-admission/get/";

    // ========== CRUD API Endpoints (Exam Types) ==========
    public static final String examtypeListUrl = "examtype/list";
    public static final String examtypeCreateUrl = "examtype/create";
    public static final String examtypeUpdateUrl = "examtype/update/(:num)";
    public static final String examtypeDeleteUrl = "examtype/delete/(:num)";
    public static final String publicexamtypeListUrl = "publicexamtype/list";
    public static final String publicexamtypeCreateUrl = "publicexamtype/create";
    public static final String publicexamtypeUpdateUrl = "publicexamtype/update/(:num)";
    public static final String publicexamtypeDeleteUrl = "publicexamtype/delete/(:num)";

    // ========== Admin API Endpoints (Screens without previous APIs) ==========
    public static final String staffDirectoryListUrl = "staff-directory/list";
    public static final String staffDirectorySearchUrl = "staff-directory/search";
    public static final String notificationAdminListUrl = "notification-admin/list";
    public static final String notificationAdminCreateUrl = "notification-admin/create";
    public static final String notificationAdminDeleteUrl = "notification-admin/delete";
    public static final String mailsmsAdminListUrl = "mailsms-admin/list";
    public static final String homeworkAdminListUrl = "homework-admin/list";
    public static final String homeworkAdminDailyUrl = "homework-admin/daily-assignment";
    public static final String libraryAdminBooksUrl = "library-admin/books";
    public static final String libraryAdminMembersUrl = "library-admin/members";
    public static final String transportAdminRoutesUrl = "transport-admin/routes";
    public static final String transportAdminVehiclesUrl = "transport-admin/vehicles";
    public static final String transportAdminPickupPointsUrl = "transport-admin/pickup-points";
    public static final String transportAdminVehicleRoutesUrl = "transport-admin/vehicle-routes";
    public static final String hostelAdminHostelsUrl = "hostel-admin/hostels";
    public static final String hostelAdminRoomsUrl = "hostel-admin/rooms";
    public static final String hostelAdminRoomTypesUrl = "hostel-admin/room-types";
    public static final String cmsAdminEventsUrl = "cms-admin/events";
    public static final String cmsAdminPagesUrl = "cms-admin/pages";
    public static final String cmsAdminMediaUrl = "cms-admin/media";
    public static final String cmsAdminMenusUrl = "cms-admin/menus";
    public static final String cmsAdminGalleryUrl = "cms-admin/gallery";
    public static final String cmsAdminBannersUrl = "cms-admin/banners";
    public static final String examAdminExamGroupsUrl = "exam-admin/exam-groups";
    public static final String examAdminScheduleUrl = "exam-admin/schedule";
    public static final String examAdminGradesUrl = "exam-admin/grades";
    public static final String examAdminMarksDivisionsUrl = "exam-admin/marks-divisions";
    public static final String lessonAdminSyllabusUrl = "lesson-admin/syllabus";
    public static final String lessonAdminLessonsUrl = "lesson-admin/lessons";
    public static final String lessonAdminTopicsUrl = "lesson-admin/topics";
    public static final String sessionAdminListUrl = "session-admin/list";
    public static final String settingsAdminGeneralUrl = "settings-admin/general";
    public static final String settingsAdminSchoolUrl = "settings-admin/school";
    public static final String settingsAdminNotificationUrl = "settings-admin/notification";
    public static final String settingsAdminSmsUrl = "settings-admin/sms";
    public static final String settingsAdminEmailUrl = "settings-admin/email";
    public static final String settingsAdminPaymentUrl = "settings-admin/payment";
    public static final String settingsAdminWhatsappUrl = "settings-admin/whatsapp";
    public static final String settingsAdminLanguagesUrl = "settings-admin/languages";
    public static final String settingsAdminModulesUrl = "settings-admin/modules";
    public static final String settingsAdminCurrencyUrl = "settings-admin/currency";
    public static final String settingsAdminUsersUrl = "settings-admin/users";
    public static final String settingsAdminRolesUrl = "settings-admin/roles";
    public static final String alumniAdminListUrl = "alumni-admin/list";
    public static final String alumniAdminEventsUrl = "alumni-admin/events";
    public static final String leaveAdminTypesUrl = "leave-admin/types";
    public static final String leaveAdminRequestsUrl = "leave-admin/requests";
    public static final String leaveAdminApproveUrl = "leave-admin/approve";
    public static final String leaveAdminRejectUrl = "leave-admin/reject";
    public static final String payrollAdminListUrl = "payroll-admin/list";
}


