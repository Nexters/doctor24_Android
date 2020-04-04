package com.nexters.doctor24.todoc.analytics

/* 지도 홈 */
const val MAP_HOSPITAL_TAP = "hospital_click"
const val MAP_PHARMACY_TAP = "pharmacy_click"
const val MAP_REFRESH = "retry_click"
const val MAP_MY_LOCATION = "camera_click"

/* 시간 필터 */
const val MAP_TIME_FILTER_OPEN = "time_filter_click"
const val MAP_TIME_FILTER_COMPLETE = "time_filter_complete_click"
const val MAP_TIME_FILTER_START_TIME_PARAM = "start" // 설정 시작시간
const val MAP_TIME_FILTER_END_TIME_PARAM = "end" // 설정 끝시간
const val MAP_TIME_FILTER_CLOSE = "time_filter_close_click"
const val MAP_TIME_FILTER_DIMMED = "time_filter_dimmed_click"
const val MAP_TIME_FILTER_RESET = "time_filter_refresh_click"
const val MAP_TIME_FILTER_START_INPUT = "time_filter_start_click"
const val MAP_TIME_FILTER_END_INPUT = "time_filter_end_click"

/* 진료 과목 */
const val MEDICAL_CATEGORY_OPEN = "medical_category_click"
const val MEDICAL_CATEGORY_TYPE = "medical_category_type_click"
const val MEDICAL_CATEGORY_TYPE_PARAM = "type"
const val MEDICAL_CATEGORY_RESET = "medical_category_refresh_click"

/* 지도 마커 */
const val MARKER_TAP = "marker_preview_click"
const val MARKER_DETAIL_OPEN = "marker_preview_detail_click"
const val MARKER_PREVIEW_ID_PARAM = "id"
const val MARKER_PREVIEW_NAME_PARAM = "name"
const val MARKER_PREVIEW_CATEGORY_PARAM = "category"
const val MARKER_PREVIEW_TYPE_PARAM = "type"
const val MARKER_PREVIEW_CALL = "marker_preview_call_click"
const val MARKER_PREVIEW_FIND_LOAD = "marker_preview_navi_click"
const val MARKER_PREVIEW_DIMMED = "marker_preview_dimmed_click"

const val MARKER_BUNDLE_TAP = "marker_cluster_click"
const val MARKER_BUNDLE_DETAIL_OPEN = "marker_cluster_detail_click"
const val MARKER_BUNDLE_CLOSE = "marker_cluster_close"

/* 지도 병원 리스트 */
const val MEDICAL_LIST_TAP = "around_list_click"
const val MEDICAL_LIST_SORT = "around_list_sort_click"

/* 병원,약국 상세 */
const val MEDICAL_DETAIL_MINI_MAP = "detail_map_click"
const val MEDICAL_DETAIL_FIND_LOAD = "detail_navi_click"
const val MEDICAL_DETAIL_CALL = "detail_call_click"
const val MEDICAL_DETAIL_CLOSE = "detail_close_click"

/* 마스크 모드 */
const val MASK_MODE_TAP = "mask_mode_click"
const val MASK_MARKER_TAP = "mask_marker_click"
const val MASK_STATE_PARAM = "state"
const val MASK_STOCK_ON_OFF = "mask_stock_click"
const val MASK_STOCK_ACTIVE_PARAM = "isActive"
const val MASK_FIND_LOAD = "mask_navi_click"
const val MASK_REFRESH = "mask_retry_click"
const val MASK_MARKER_CLOSE = "mask_marker_close"
const val MASK_MOSE_CLOSE = "mask_mode_close"