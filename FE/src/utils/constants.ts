// export const

export const BASE_URL_TELCO = 'http://api-dev.mservice.io:22454';
export const BASE_URL_TELCO_PRO = 'http://api.mservice.io:22454';

export const API_TELCO = {
  BOOT_HANGOUT:
    'https://chat.googleapis.com/v1/spaces/AAAAQGyebrc/messages?key=AIzaSyDdI0hCZtE6vySjMm-WEfRq3CPzqKqqsHI&token=Ve6aznvvX4rmZYURsposbf6MEe5RHVisB1Kxdo6NJjU%3D',
  BOOT_HANGOUT_PRO:
    'https://chat.googleapis.com/v1/spaces/AAAAQGyebrc/messages?key=AIzaSyDdI0hCZtE6vySjMm-WEfRq3CPzqKqqsHI&token=YrRdooQjvveM07JZhSECtfrIvIxz9SXQd_ROO3JNSwQ%3D',
  GET_BALANCING: '/web-tool-app-http-billpay-telco/telco/webTool/getBalancing',
  GET_WEB_TOOL_CONFIG: '/web-tool-app-http-billpay-telco/telco/webTool/getWebToolConfig',
  GET_WARE_HOUSELIST: '/web-tool-app-http-billpay-telco/telco/webTool/getWarehouseList',
  GET_HISTORY: '/web-tool-app-http-billpay-telco/telco/webTool/getHistory',
  POST_UPDATE_CONFIG: '/web-tool-app-http-billpay-telco/telco/webTool/updateConfig',
  UPDATE_WAREHOUSE_MANAGER: '/web-tool-app-http-billpay-telco/telco/webTool/updateWarehouseManager',
  UPDATE_ON_OFF_PROVIDER: '/web-tool-app-http-billpay-telco/telco/webTool/updateOnOffProvider',
};

export const GROUP_TOPUP = [
  'topup_viettel',
  'topup_mobifone',
  'topup_vinaphone',
  'topup_vietnamobile',
  'topup_gmobile',
];

export const GROUP_BALANCING_BUYCARD = [
  // Mobifone
  'IMD_MOBIFONE',
  'WHYPAY_MOBIFONE',
  'OCTA_MOBIFONE',
  'Mservice',
  // Vinaphone
  'IMD_VINAPHONE',
  'OCTA_VINAPHONE',
  'WHYPAY_VINAPHONE',
  // Viettel
  'IMD_VIETTEL',
  'IPAY_VIETTEL',
  'WHYPAY_VIETTEL',
  'ZOTA_VIETTEL',
  'OCTA_VIETTEL',
  'OCTA_VIETTEL_TEST',
  // Vietnamobile
  'IMD_VIETNAMOBILE',
  'WHYPAY_VIETNAMMOBILE',
  // Gmobile
  'IMD_GMOBILE',
  // Mobifone card data
  'IMD_DATA_IMD_MBP',
  // Vinaphone card data
  'IMD_DATA_IMD_VNP',
  // Viettel card data
  'IMD_DATA_IMD_VIETTEL',
];

export const GROUP_BALANCING_BUYCARD_DATA = [
  // Mobifone card data
  'IMD_DATA_IMD_MBP',
  // Vinaphone card data
  'IMD_DATA_IMD_VNP',
  // Viettel card data
  'IMD_DATA_IMD_VIETTEL',
];

export const GROUP_BALANCING_TOPUP = [
  // Mobifone
  // 'mservice',
  // 'mservice1',
  // 'mservice10',
  // 'mservice11',
  'mservice12',
  // 'mservice2',
  // 'mservice3',
  // 'mservice4',
  // 'mservice5',
  // 'mservice6',
  // 'mservice7',
  // 'mservice8',
  // 'mservice9',
  // // Vinaphone
  // 'vinaphone_Sim_0917004855',
  // 'vinaphone_Sim_6686',
  // // Viettel
  // 'OCTA',
  // 'WHYPAY',
  // 'FIVI',
  // // Vietnamobile
  // 'VIETNAMOBILE',
  // // Gmobile
  // 'GMOBILE',
];

export const GROUP_BALANCING_TOPUP_DATA = [
  // Mobifone
  'MPS',
  'IMD_DATA_MOBILE',
  // Vinaphone
  'Mydata',
  // Viettel
  'imedia',
];

export const SERVICE_CATEGORY = {
  TOPUP: 'topup',
  TOPUP_DATA: 'topupData',
  BUYCARD: 'buycard',
  BUYCARD_DATA: 'buycardData',
};

export const GROUP_TOPUP_DATA = ['topup_data_mobi', 'topup_data_viettel', 'topup_data_vina'];

export const GROUP_BUYCARD = [
  'epay_mobifone',
  'epay_vinafone',
  'epay_viettel',
  'epay_beeline',
  'epay_vietnamobile',
];

export const GROUP_BUYCARD_DATA = [
  'buycard_data_viettel',
  'buycard_data_mobifone',
  'buycard_data_gmobile',
  'buycard_data_vietnamobile',
  'buycard_data_vinaphone',
];
