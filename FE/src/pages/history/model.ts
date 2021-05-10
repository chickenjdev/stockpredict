import { Effect, Reducer } from 'umi';
import { getHistory } from './service';
export interface ModelType {
  namespace: string;
  state: any;
  effects: {
    fetchHistory: Effect;
  };
  reducers: {
    save: Reducer<any>;
  };
}

const Model: ModelType = {
  namespace: 'history',

  state: {},

  effects: {
    *fetchHistory({ params, callback }, { call, put }) {
      const response = yield call(getHistory, params);
      if (callback && typeof callback === 'function') {
        callback({ dataHistory: response });
      } else {
        yield put({
          type: 'save',
          payload: response,
        });
      }
    },
  },

  reducers: {
    save(state, { payload }) {
      return {
        ...state,
        dataHistory: payload,
      };
    }
  },
};

export default Model;
