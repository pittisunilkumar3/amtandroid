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
}


