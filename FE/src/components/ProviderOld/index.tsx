/* eslint-disable react-hooks/rules-of-hooks */
/* eslint-disable @typescript-eslint/no-use-before-define */
/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable-next-line react-hooks/rules-of-hooks */

import type { FC } from 'react';
import React, { useState, useCallback, useEffect, useRef, useLayoutEffect } from 'react';
import { Table, Tag, Switch, List, Card, Button, Divider, Spin, notification, Modal } from 'antd';
import { useIntl, connect, formatMessage } from 'umi';
import { DrawerProduct } from '@/components';
import 'antd/dist/antd.css';
import { ExclamationCircleOutlined } from '@ant-design/icons';
import { getDataLocal } from '@/utils/utils';

const { confirm } = Modal;

const Header = ({ useAdd }: any) => {
  const intl = useIntl();

  const onAdd = useCallback(() => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    // eslint-disable-next-line @typescript-eslint/no-unused-expressions
    useAdd && typeof useAdd === 'function' && useAdd();
  }, [useAdd]);

  return (
    <div>
      <div className="row">
        <Button style={{ marginRight: 10 }}>
          {intl.formatMessage({
            id: 'pages.provider.edit',
            defaultMessage: 'Edit',
          })}
        </Button>
        <Button type="primary" onClick={onAdd}>
          {intl.formatMessage({
            id: 'pages.provider.add',
            defaultMessage: 'Add',
          })}
        </Button>
      </div>
    </div>
  );
};

const ItemProvider = ({ data, useAddProduct, rowSelection = undefined, useSwitchChange }: any) => {
  const columns = [
    {
      title: formatMessage({
        id: 'pages.provider.no',
        defaultMessage: 'No.',
      }),
      dataIndex: 'key',
      key: 'key',
    },

    {
      title: formatMessage({
        id: 'pages.provider.price',
        defaultMessage: 'Mệnh giá',
      }),
      dataIndex: 'price',
      key: 'price',
      render: (text: number) => {
        return <b>{`${text}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}</b>;
      },
    },
    {
      title: formatMessage({
        id: 'pages.provider.mainWarehouse',
        defaultMessage: 'Ưu tiên 1',
      }),
      dataIndex: 'mainWarehouse',
      key: 'mainWarehouse',
      render: (item: any) => {
        const { name, isMain } = item || {};
        if (isMain) {
          return (
            <>
              <Tag color={'blue'} key={name}>
                {name.toUpperCase()}
              </Tag>
            </>
          );
        }
        return <>{name}</>;
      },
    },
    {
      title: formatMessage({
        id: 'pages.provider.extraWarehouse',
        defaultMessage: 'Ưu tiên 2',
      }),
      dataIndex: 'extraWarehouse',
      key: 'extraWarehouse',
      render: (item: any) => {
        const { name, isMain } = item || {};
        if (isMain) {
          return (
            <>
              <Tag color={'blue'} key={name}>
                {name.toUpperCase()}
              </Tag>
            </>
          );
        }
        return <>{name}</>;
      },
    },
    {
      title: formatMessage({
        id: 'pages.provider.active',
        defaultMessage: 'Active',
      }),
      key: 'active',
      dataIndex: 'active',
      render: (active: any, record: any) => (
        <Switch
          // disabled={true}
          onChange={() => handleSwitchChange(record)}
          checkedChildren={formatMessage({
            id: 'app.settings.open',
            defaultMessage: 'ON',
          })}
          unCheckedChildren={formatMessage({
            id: 'app.settings.close',
            defaultMessage: 'OFF',
          })}
          checked={active}
        />
      ),
    },
  ];

  const handleSwitchChange = (record: any) => {
    useSwitchChange(record);
  };

  return (
    <List.Item>
      <Card
        title={data.title.toUpperCase()}
        headStyle={{ fontWeight: 'bold', backgroundColor: '#ffcccc' }}
        extra={<Header useAdd={useAddProduct} style={{ margin: 0 }} />}
        bodyStyle={{ margin: 0, padding: 0 }}
      >
        <Table
          columns={columns}
          dataSource={data.bills}
          pagination={{
            hideOnSinglePage: true,
            position: [],
          }}
          rowSelection={rowSelection}
        />
      </Card>
      <Divider plain>***</Divider>
    </List.Item>
  );
};

interface ProviderOldProps {
  topup: any;
  dispatch: Dispatch<any>;
  isTopup: any;
  groupTopup: any;
  nameReplace: string;
}
const ProviderOld: FC<ProviderOldProps> = ({
  topup,
  dispatch,
  isTopup,
  groupTopup,
  nameReplace,
}: any) => {
  const [showAddProduct, setShowAddProduct] = useState(false);
  const [dataProvider, setProvider] = useState(new Array());
  const [isShowSpin, setLoading] = useState(true);
  const intl = useIntl();
  const reqRef = useRef<number>(0);

  const useAddProduct = useCallback(() => {
    setShowAddProduct(!showAddProduct);
  }, [setShowAddProduct, showAddProduct]);

  const useClose = () => {
    setShowAddProduct(false);
  };

  const useNotiHangout = useCallback(
    (response, active, reference2, price) => {
      const priceVND = price.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
      const { resultCode } = response || {};
      const userProfile = getDataLocal('login_successful');
      const { profileObj } = (userProfile as any) || {};
      const { email, name } = profileObj || {};
      const message =
        `//*${reference2}*//: *${name}* (${email})` +
        ` đã ${active ? '*`OFF`*' : '*`ON`* '} Mệnh giá : *${priceVND}*` +
        ` - ${resultCode === 0 ? '`SUCCESS`' : '`ERROR`'}`;
      if (dispatch) {
        dispatch({
          type: 'topup/alertHangout',
          params: message,
        });
      }
    },
    [dispatch],
  );

  const useSwitchChange = useCallback(
    (record: any) => {
      const { active, reference2, price } = record || {};
      const title = `Bạn muốn ${active ? 'OFF' : 'ON'} mệnh giá ${price}?`;
      confirm({
        title,
        icon: <ExclamationCircleOutlined />,
        content: reference2,
        onOk() {
          if (dispatch) {
            reqRef.current = requestAnimationFrame(() => {
              dispatch({
                type: 'topup/setTopupOld',
                params: {
                  queue: isTopup
                    ? 'ha_connector_telco_router_topup_req'
                    : 'ha_connector_telco_router_softpin_req',
                  data: {
                    reference1: active ? 'OFF' : 'ON',
                    reference2,
                    // eslint-disable-next-line radix
                    amount: parseInt(price),
                    type: 106,
                  },
                },
                callback: (response: any) => {
                  const { resultCode, resultMessage } = response || {};
                  notification[resultCode === 0 ? 'success' : 'error']({
                    message: resultCode === 0 ? 'Cập nhật thành công!' : resultMessage,
                  });
                  useNotiHangout(response, active, reference2, price);
                  dispatch({
                    type: isTopup ? 'topup/getTopupOld' : 'topup/getCardOld',
                    params: {
                      queue: isTopup
                        ? 'ha_connector_telco_router_topup_req'
                        : 'ha_connector_telco_router_softpin_req',
                      data: {
                        reference1: 'GET_STATUS',
                        reference2: '1',
                        amount: 500000,
                        type: 106,
                      },
                    },
                  });
                },
              });
            });
          }
        },
        onCancel() {},
      });
    },
    [dispatch, isTopup, useNotiHangout],
  );

  const useData = useCallback(
    // eslint-disable-next-line @typescript-eslint/no-shadow
    (topup) => {
      const { billList = [] } = (isTopup ? topup.topupOld : (topup.cardOld as any)) || {};
      const listTopup = billList.filter((item: {}) => {
        const { reference2 } = (item as any) || {};
        return groupTopup.indexOf(reference2) > -1;
      });
      const listProvider = Array();
      listTopup.forEach((item = {}, _index: number) => {
        const { extras = {}, reference2 = '' } = (item as any) || {};
        const provider = {} as any;
        provider.title = reference2.replace(nameReplace, '');
        provider.bills = Array();
        let index = 0;
        // eslint-disable-next-line no-restricted-syntax
        for (const [key, value] of Object.entries(extras)) {
          let infoStore = {} as any;
          if (typeof value === 'string') {
            infoStore = JSON.parse(value);
          }
          const { store1, store2, activeStore, status } = (infoStore as any) || {};
          const bill = {} as any;
          // eslint-disable-next-line no-multi-assign
          bill.key = index += 1;
          bill.reference2 = reference2;
          bill.price = key;
          bill.mainWarehouse = {
            name: store1 || '',
            isMain: activeStore === store1,
          };
          bill.extraWarehouse = {
            name: store2 || '',
            isMain: activeStore === store2,
          };
          if (status === 1) {
            bill.active = true;
          } else {
            bill.active = false;
          }
          provider.bills.push(bill);
        }
        listProvider.push(provider);
      });
      setProvider(listProvider);
      setLoading(false);
    },
    [setLoading, setProvider, isTopup, groupTopup, nameReplace],
  );

  useEffect(() => {
    if (dispatch) {
      reqRef.current = requestAnimationFrame(() => {
        dispatch({
          type: isTopup ? 'topup/getTopupOld' : 'topup/getCardOld',
          params: {
            queue: isTopup
              ? 'ha_connector_telco_router_topup_req'
              : 'ha_connector_telco_router_softpin_req',
            data: {
              reference1: 'GET_STATUS',
              reference2: '1',
              amount: 500000,
              type: 106,
            },
          },
        });
      });
    }
    return () => {
      cancelAnimationFrame(reqRef.current);
    };
  }, [dispatch, isTopup, reqRef, setProvider]);

  useLayoutEffect(() => {
    useData(topup);
    return () => {};
  }, [topup, useData]);

  return (
    <>
      <div>
        <Spin spinning={isShowSpin}>
          <List
            grid={{ gutter: 50, column: 2 }}
            dataSource={dataProvider}
            renderItem={(item) => (
              <ItemProvider
                data={item}
                useSwitchChange={useSwitchChange}
                useAddProduct={useAddProduct}
              />
            )}
          />
          <DrawerProduct visible={showAddProduct} onCloseView={useClose} />
        </Spin>
      </div>
    </>
  );
};

export default connect(
  ({
    topup,
    loading,
  }: {
    topup: any;
    loading: {
      effects: { [key: string]: boolean };
    };
  }) => ({
    topup,
    loading: loading.effects['topup/getTopupOld'],
  }),
)(ProviderOld);
