/* eslint-disable @typescript-eslint/no-unused-vars */
import type { MenuDataItem } from '@ant-design/pro-layout';
import { DefaultFooter, getMenuData, getPageTitle } from '@ant-design/pro-layout';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import type { ConnectProps } from 'umi';
import { Link, SelectLang, useIntl, connect, FormattedMessage } from 'umi';
import React from 'react';
import type { ConnectState } from '@/models/connect';
import logo from '../assets/logo.svg';
import styles from './UserLayout.less';

export type UserLayoutProps = {
  breadcrumbNameMap: Record<string, MenuDataItem>;
} & Partial<ConnectProps>;

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

const Footer = () => {
  return <DefaultFooter copyright={`${new Date().getFullYear()} StockPrediction`} links={[]} />;
};

const UserLayout: React.FC<UserLayoutProps> = (props) => {
  const {
    route = {
      routes: [],
    },
  } = props;
  const { routes = [] } = route;
  const {
    children,
    location = {
      pathname: '',
    },
  } = props;
  const { formatMessage } = useIntl();
  const { breadcrumb } = getMenuData(routes);
  const title = getPageTitle({
    pathname: location.pathname,
    formatMessage,
    breadcrumb,
    ...props,
  });
  return (
    <HelmetProvider>
      <Helmet>
        <title>{title}</title>
        <meta name="description" content={title} />
      </Helmet>

      <div className={styles.container}>
        <div className={styles.lang}>
          <SelectLang
            postLocalesData={(_locales: any[]) => {
              return defaultLangUConfigMap;
            }}
          />
        </div>
        <div className={styles.content}>
          <div className={styles.top}>
            <div className={styles.header}>
              <Link to="/">
                <img alt="logo" className={styles.logo} src={logo} />
                <span className={styles.title}>StockPrediction</span>
              </Link>
            </div>
            <div className={styles.desc}>
              <FormattedMessage
                id="pages.layouts.userLayout.title"
                defaultMessage="StockPrediction"
              />
            </div>
          </div>
          {children}
        </div>
        <Footer />
      </div>
    </HelmetProvider>
  );
};

export default connect(({ settings }: ConnectState) => ({ ...settings }))(UserLayout);
