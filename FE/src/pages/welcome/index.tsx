import React from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import { Card, Alert, Typography } from 'antd';
import { useIntl, FormattedMessage } from 'umi';
import styles from './style.less';

const CodePreview: React.FC = ({ children }) => (
  <pre className={styles.pre}>
    <code>
      <Typography.Text copyable>{children}</Typography.Text>
    </code>
  </pre>
);

export default (): React.ReactNode => {
  const intl = useIntl();
  return (
    <PageContainer>
      <Card>
        <Alert
          message={intl.formatMessage({
            id: 'pages.welcome.alertMessage',
            defaultMessage: 'StockPrediction',
          })}
          type="success"
          showIcon
          banner
          style={{
            margin: -12,
            marginBottom: 24,
          }}
        />
        <Typography.Text strong>
          <FormattedMessage
            id="pages.welcome.advancedComponent"
            defaultMessage="Install Chromium"
          />{' '}
        </Typography.Text>
        <CodePreview>https://download-chromium.appspot.com/</CodePreview>
        <Typography.Text
          strong
          style={{
            marginBottom: 12,
          }}
        >
          <FormattedMessage id="pages.welcome.advancedLayout" defaultMessage="Run Terminal Mac" />{' '}
        </Typography.Text>
        <CodePreview>
          open -n -a /Applications/Chromium.app/Contents/MacOS/Chromium --args
          --user-data-dir="/tmp/chrome_dev_test" --disable-web-security
        </CodePreview>

        <Typography.Text
          strong
          style={{
            marginBottom: 12,
          }}
        >
          <FormattedMessage id="pages.welcome.windows" defaultMessage="Run Terminal Windows" />{' '}
        </Typography.Text>
        <CodePreview>
          "C:\Program Files (x86)\chrome-win\chrome-win\chrome.exe" --disable-web-security
          --disable-gpu --user-data-dir=~/chromeTemp
        </CodePreview>
      </Card>
    </PageContainer>
  );
};
