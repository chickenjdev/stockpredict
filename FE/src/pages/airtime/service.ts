import { extend } from 'umi-request';
import request from '@/utils/request';
import { API_TELCO } from '@/utils/constants';
import MServiceRequest, { environment } from '@/utils/MServiceRequest';
import { isProduction } from '@/utils/utils';

const extendRequest = extend({
  timeout: 10000,
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
});

export type DataParamsType = {
  reference1: string;
  reference2: string;
  amount: number;
  type: number;
};

export type TopupOldParamsType = {
  queue: string;
  data: DataParamsType;
};

export async function getDataTopupOld(params: TopupOldParamsType) {
  return request('/rabbitClient', {
    method: 'POST',
    data: params,
    // prefix: environment(),
  });
}

export async function setDataTopupOld(params: TopupOldParamsType) {
  return request('/rabbitClient', {
    method: 'POST',
    data: params,
    // prefix: environment(),
  });
}

export async function alertHangout(params: string) {
  return extendRequest(isProduction() ? API_TELCO.BOOT_HANGOUT_PRO : API_TELCO.BOOT_HANGOUT, {
    method: 'POST',
    data: JSON.stringify({
      text: params,
    }),
  });
}

export async function getConfigTelco(params: any) {
  return MServiceRequest(API_TELCO.GET_WEB_TOOL_CONFIG, {
    method: 'GET',
    params,
    prefix: environment(),
    mode: 'cors',
  });
}
