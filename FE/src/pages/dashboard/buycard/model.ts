/* eslint-disable @typescript-eslint/no-unused-vars */
import type { Effect, Reducer } from 'umi';
import { getBalancing } from './service';

export interface ModelType {
  namespace: string;
  state: any;
  effects: {
    getBalancing: Effect;
  };
  reducers: {
    save: Reducer<any>;
    clear: Reducer<any>;
  };
}

const initState = {};

const Model: ModelType = {
  namespace: 'dashboardBuycard',

  state: initState,

  effects: {
    *getBalancing(_, { call, put }) {
      const response = yield call(getBalancing);
      yield put({
        type: 'save',
        payload: response,
      });
    },
  },

  reducers: {
    save(state, { payload }) {
      return {
        ...state,
        result: payload,
      };
    },
    clear() {
      return initState;
    },
  },
};

export default Model;
