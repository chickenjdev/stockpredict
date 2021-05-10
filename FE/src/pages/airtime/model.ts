/* eslint-disable @typescript-eslint/no-unused-vars */
import { Effect, Reducer } from 'umi';
import { message } from 'antd';
import type { TopupOld } from './data.d';
import { getDataTopupOld, setDataTopupOld, alertHangout, getConfigTelco } from './service';

export interface ModelType {
  namespace: string;
  state: TopupOld;
  effects: {
    getTopupOld: Effect;
    getCardOld: Effect;
    setTopupOld: Effect;
    alertHangout: Effect;
    getConfigTelco: Effect;
  };
  reducers: {
    saveTopupOld: Reducer<any>;
    saveCardOld: Reducer<any>;
    saveConfigTelco: Reducer<any>;
    clear: Reducer<TopupOld>;
    result: Reducer<any>;
  };
}

const initState = {
  smsContent: [],
  extras: {},
  request: {},
  billList: [],
  resultCode: 0,
  resultMessage: [],
} as any;

const Model: ModelType = {
  namespace: 'topup',

  state: initState,

  effects: {
    *getTopupOld({ params }, { call, put }) {
      const response = yield call(getDataTopupOld, params);
      yield put({
        type: 'saveTopupOld',
        payload: response,
      });
    },
    *getCardOld({ params }, { call, put }) {
      const response = yield call(getDataTopupOld, params);
      yield put({
        type: 'saveCardOld',
        payload: response,
      });
    },
    *setTopupOld({ params, callback }, { call, put }) {
      const response = yield call(setDataTopupOld, params);
      if (callback && typeof callback === 'function') {
        callback(response);
      }
      yield put({
        type: 'result',
        payload: response,
      });
    },
    *alertHangout({ params }, { call, put }) {
      const response = yield call(alertHangout, params);
    },
    *getConfigTelco({ params }, { call, put }) {
      const response = yield call(getConfigTelco, params);
      yield put({
        type: 'saveConfigTelco',
        payload: response,
      });
    },
  },
  reducers: {
    saveTopupOld(state, { payload }) {
      return {
        ...state,
        topupOld: payload,
      };
    },
    saveCardOld(state, { payload }) {
      return {
        ...state,
        cardOld: payload,
      };
    },
    clear() {
      return initState;
    },
    result(state, { payload }) {
      return {
        ...state,
        result: payload,
      };
    },
    saveConfigTelco(state, { payload }) {
      return {
        ...state,
        configTelco: payload,
      };
    },
  },
};

export default Model;
