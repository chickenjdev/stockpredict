/* eslint-disable @typescript-eslint/no-unused-vars */
import { Tooltip, Tag } from 'antd';
import type { Settings as ProSettings } from '@ant-design/pro-layout';
import { QuestionCircleOutlined } from '@ant-design/icons';
import React from 'react';
import type { ConnectProps } from 'umi';
import { connect, SelectLang } from 'umi';
import type { ConnectState } from '@/models/connect';
import Avatar from './AvatarDropdown';
import HeaderSearch from '../HeaderSearch';
import styles from './index.less';
import { getDataLocal } from '@/utils/utils';

export type GlobalHeaderRightProps = {
  theme?: ProSettings['navTheme'] | 'realDark';
} & Partial<ConnectProps> &
  Partial<ProSettings>;
const ENVTagColor = {
  development: 'orange',
  staging: 'green',
  production: '#87d068',
};

const defaultLangUConfigMap = [
  {
    lang: 'en-US',
    label: 'English',
    icon: '🇺🇸',
    title: 'Language',
  },
  {
    lang: 'vi-VN',
    label: 'Tiếng Việt',
    icon: '🇻🇳',
    title: 'Ngôn ngữ',
  },
];

const GlobalHeaderRight: React.FC<GlobalHeaderRightProps> = (props) => {
  const { theme, layout } = props;
  let className = styles.right;

  if (theme === 'dark' && layout === 'top') {
    className = `${styles.right}  ${styles.dark}`;
  }

  const environment = getDataLocal('environment');

  return (
    <div className={className}>
      <HeaderSearch
        className={`${styles.action} ${styles.search}`}
        placeholder="predictStock.vn"
        defaultValue="predictStock"
        options={[
          {
            label: <a href="http://predictStock.vn/">predictStock</a>,
            value: 'predictStock',
          },
          {
            label: <a href="http://predictStock.vn/">Warehouse</a>,
            value: 'Warehouse',
          },
          {
            label: <a href="http://predictStock.vn/">Airtime</a>,
            value: 'Airtime',
          },
          {
            label: <a href="http://predictStock.vn/">Data</a>,
            value: 'Data',
          },
        ]} // onSearch={value => {
        //   //console.log('input', value);
        // }}
      />
      <Tooltip title="predictStock.vn">
        <a
          style={{
            color: 'inherit',
          }}
          target="_blank"
          href="http://predictStock.vn/"
          rel="noopener noreferrer"
          className={styles.action}
        >
          <QuestionCircleOutlined />
        </a>
      </Tooltip>
      {/* <NoticeIconView /> */}
      <Avatar menu />
      {environment && Array.isArray(environment) && environment.length > 0 && environment[0] && (
        <span>
          <Tag color={ENVTagColor[environment[0]]}>{environment[0]}</Tag>
        </span>
      )}
      <SelectLang
        className={styles.action}
        postLocalesData={(_locales: any[]) => {
          return defaultLangUConfigMap;
        }}
      />
    </div>
  );
};

export default connect(({ settings }: ConnectState) => ({
  theme: settings.navTheme,
  layout: settings.layout,
}))(GlobalHeaderRight);
