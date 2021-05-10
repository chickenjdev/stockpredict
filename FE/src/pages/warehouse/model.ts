import { Effect, Reducer } from 'umi';
import { getWarehouseList, updateConfig } from './service';
export interface ModelType {
  namespace: string;
  state: any;
  effects: {
    fetchWarehouseList: Effect;
    updateConfig: Effect;
  };
  reducers: {
    save: Reducer<any>;
  };
}

const Model: ModelType = {
  namespace: 'warehouse',

  state: {},

  effects: {
    *fetchWarehouseList({ params }, { call, put }) {
      const response = yield call(getWarehouseList, params);
      yield put({
        type: 'save',
        payload: response,
      });
    },
    *updateConfig({ params, callback }, { call, put }) {
      const response = yield call(updateConfig, params);
      if (callback && typeof callback === 'function') {
        callback(response)
      }
    }
  },

  reducers: {
    save(state, { payload }) {
      return {
        ...state,
        warehouseList: payload,
      };
    }
  },
};

export default Model;
