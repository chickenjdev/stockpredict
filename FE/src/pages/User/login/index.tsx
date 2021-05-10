/* eslint-disable @typescript-eslint/no-unused-vars */
import React, { useState, useEffect, useCallback } from 'react';
import { Select } from 'antd';
import ProForm from '@ant-design/pro-form';
import { useIntl, connect } from 'umi';
import type { Dispatch } from 'umi';
import type { StateType } from '@/models/login';
import type { ConnectState } from '@/models/connect';
import GoogleLogin from 'react-google-login';
import styles from './index.less';
import { isProduction, setDataLocal } from '@/utils/utils';

const { Option } = Select;

export type LoginProps = {
  dispatch: Dispatch;
  userLogin: StateType;
  submitting?: boolean;
};

const Login: React.FC<LoginProps> = (props) => {
  const [environment, setEnvironment] = useState<string>(
    isProduction() ? 'production' : 'development',
  );
  const intl = useIntl();

  const onFinish = (response: any) => {
    const { dispatch } = props;
    dispatch({
      type: 'login/login',
      response: { ...response },
      environment,
    });
  };

  const onFinishFailed = (errorInfo: any) => {
    // console.log("Error:", errorInfo);
  };

  const buttonLogin = () => {
    return (
      <>
        <ProForm.Item>
          <GoogleLogin
            clientId="529813498026-00el4dk5dt1ocf927203mra61kqncm0p.apps.googleusercontent.com"
            buttonText={intl.formatMessage({
              id: 'pages.login.title',
              defaultMessage: 'Sign in with Google',
            })}
            onSuccess={onFinish}
            onFailure={onFinishFailed}
            className={styles.button}
            // cookiePolicy={"single_host_origin"}
            isSignedIn={false}
          />
        </ProForm.Item>
      </>
    );
  };

  const useChange = useCallback((value) => {
    // setEnvironment(value || 'production');
    setDataLocal(value || 'production', 'environment');
  }, []);

  useEffect(() => {
    setDataLocal(isProduction() ? 'production' : 'development', 'environment');
    return () => {};
  }, [setEnvironment]);

  return (
    <div className={styles.main}>
      <ProForm
        submitter={{
          // eslint-disable-next-line @typescript-eslint/no-unused-vars
          render: (_) => {
            return buttonLogin();
          },
        }}
      >
        <div>
          <ProForm.Item
            rules={[
              {
                required: true,
              },
            ]}
          >
            <Select
              showSearch
              size={'large'}
              style={{ width: 350 }}
              placeholder="Search to Select"
              optionFilterProp="children"
              filterOption={(input, option) =>
                option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
              }
              filterSort={(optionA, optionB) =>
                optionA.children.toLowerCase().localeCompare(optionB.children.toLowerCase())
              }
              defaultValue={environment}
              onChange={useChange}
              disabled={true}
            >
              <Option value="development">
                {intl.formatMessage({
                  id: 'pages.login.development',
                  defaultMessage: 'Development',
                })}
              </Option>
              <Option value="staging">
                {intl.formatMessage({
                  id: 'pages.login.staging',
                  defaultMessage: 'Staging',
                })}
              </Option>
              <Option value="production">
                {intl.formatMessage({
                  id: 'pages.login.production',
                  defaultMessage: 'Production',
                })}
              </Option>
            </Select>
          </ProForm.Item>
        </div>
      </ProForm>
    </div>
  );
};

export default connect(({ login, loading }: ConnectState) => ({
  userLogin: login,
  submitting: loading.effects['login/login'],
}))(Login);
