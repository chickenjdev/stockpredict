/* eslint-disable @typescript-eslint/no-unused-vars */
import { stringify } from 'querystring';
import { Reducer, Effect, history } from 'umi';
import { setAuthority } from '@/utils/authority';
import { setDataLocal, getPageQuery, clearAll } from '@/utils/utils';
import { message } from 'antd';

export type StateType = {
  status?: 'ok' | 'error';
  type?: string;
  currentAuthority?: 'user' | 'guest' | 'admin';
};

export type LoginModelType = {
  namespace: string;
  state: StateType;
  effects: {
    login: Effect;
    logout: Effect;
  };
  reducers: {
    changeLoginStatus: Reducer<StateType>;
  };
};

const Model: LoginModelType = {
  namespace: 'login',

  state: {
    status: undefined,
  },

  effects: {
    *login({ response, environment }, { call, put }) {
      response.status = 'ok';
      response.type = 'browser';
      yield put({
        type: 'changeLoginStatus',
        payload: response,
        environment,
      });
      // Login successfully
      if (response.status === 'ok') {
        const urlParams = new URL(window.location.href);
        const params = getPageQuery();
        message.success('🎉 🎉 🎉 login successful');
        let { redirect } = params as { redirect: string };
        if (redirect) {
          const redirectUrlParams = new URL(redirect);
          if (redirectUrlParams.origin === urlParams.origin) {
            redirect = redirect.substr(urlParams.origin.length);
            if (redirect.match(/^\/.*#/)) {
              redirect = redirect.substr(redirect.indexOf('#') + 1);
            }
          } else {
            window.location.href = '/';
            return;
          }
        }
        history.replace(redirect || '/');
      }
    },

    logout() {
      const { redirect } = getPageQuery();
      // Note: There may be security issues, please note
      if (window.location.pathname !== '/user/login' && !redirect) {
        clearAll();
        history.replace({
          pathname: '/user/login',
          search: stringify({
            redirect: window.location.href,
          }),
        });
      }
    },
  },

  reducers: {
    changeLoginStatus(state, { payload, environment }) {
      setAuthority('admin'); // payload.currentAuthority
      setDataLocal(payload, 'login_successful');
      setAuthority('admin'); // payload.currentAuthority
      setAuthority('admin'); // payload.currentAuthority
      setAuthority('admin'); // payload.currentAuthority
      return {
        ...state,
        status: payload.status,
        type: payload.type,
      };
    },
  },
};

export default Model;
