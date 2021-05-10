/* eslint-disable @typescript-eslint/no-shadow */
/* eslint-disable react-hooks/rules-of-hooks */
/* eslint-disable @typescript-eslint/no-use-before-define */
/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable-next-line react-hooks/rules-of-hooks */

import type { FC } from 'react';
import React, { useState, useCallback, useEffect, useRef, useLayoutEffect } from 'react';
import {
  Table,
  Tag,
  Switch,
  List,
  Card,
  Button,
  Divider,
  Spin,
  notification,
  Space,
  Select,
} from 'antd';
import { useIntl, connect, formatMessage } from 'umi';
import { CollectionCreateForm, DrawerProduct } from '@/components';
import 'antd/dist/antd.css';
import { getDataLocal } from '@/utils/utils';
import { API_TELCO } from '@/utils/constants';
import { environment } from '@/utils/MServiceRequest';
const { Option } = Select;

const Header = ({ useEditProduct }: any) => {
  const intl = useIntl();

  const useEdit = useCallback(() => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    // eslint-disable-next-line @typescript-eslint/no-unused-expressions
    useEditProduct && typeof useEditProduct === 'function' && useEditProduct();
  }, [useEditProduct]);

  return (
    <div>
      <div className="row">
        <Button type="primary" onClick={useEdit}>
          {intl.formatMessage({
            id: 'pages.provider.edit',
            defaultMessage: 'Edit',
          })}
        </Button>
      </div>
    </div>
  );
};

const ItemProvider = ({ data, useEditProduct, useSwitchChange }: any) => {
  const { isOffProvider, serviceId } = data || {};
  const [selectedKeys, setKeys] = useState([]);

  const mSelectedRows = useRef<any>([]);

  const columns = [
    {
      title: formatMessage({
        id: 'pages.provider.no',
        defaultMessage: 'No.',
      }),
      dataIndex: 'key',
      key: 'key',
      width: 70,
      align: 'center',
    },

    {
      title: formatMessage({
        id: 'pages.provider.price',
        defaultMessage: 'Mệnh giá',
      }),
      dataIndex: 'price',
      key: 'price',
      align: 'center',
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
      align: 'center',
      render: (active: any, record: any) => (
        <Switch
          // disabled={true}
          onChange={() => handleSwitchChange(record, serviceId)}
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
    {
      title: 'Action',
      key: 'action',
      align: 'center',
      render: (item: any) => (
        <Space size="middle">
          <Button onClick={() => useAction(item)}>Edit</Button>
        </Space>
      ),
    },
  ];

  const useAction = (record: any) => {
    if (useEditProduct && typeof useEditProduct === 'function') {
      useEditProduct(record);
    }
  };

  const rowSelection = {
    selectedRowKeys: selectedKeys,
    onChange: (selectedRowKeys: any, selectedRows: any) => {
      console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
      mSelectedRows.current = selectedRows;
      setKeys(selectedRowKeys);
    },
  };

  const useEdit = useCallback(() => {}, []);

  const handleSwitchChange = (record: any, serviceId: string) => {
    let offProvider = null;
    if (mSelectedRows.current.length === data.bills.length) {
      offProvider = isOffProvider === 0;
    }
    useSwitchChange(
      mSelectedRows.current.length > 0 ? mSelectedRows.current : [record],
      serviceId,
      offProvider,
    );
  };

  return (
    <List.Item>
      <Card
        title={data.title.toUpperCase()}
        headStyle={{ fontWeight: 'bold', backgroundColor: isOffProvider === 0 ? '#ffcccc' : 'red' }}
        // extra={<Header useEditProduct={useEdit} style={{ margin: 0 }} />}
        bodyStyle={{ margin: 0, padding: 0 }}
      >
        <Table
          bordered={true}
          columns={columns}
          dataSource={data.bills}
          pagination={{
            hideOnSinglePage: true,
            position: [],
          }}
          rowSelection={{
            type: 'checkbox',
            ...rowSelection,
          }}
          tableLayout={'fixed'}
        />
      </Card>
      <Divider plain>***</Divider>
    </List.Item>
  );
};

interface ProviderProps {
  topup: any;
  warehouse: any;
  dispatch: Dispatch<any>;
  isTopup: any;
  groupTopup: any;
}

const Provider: FC<ProviderProps> = ({ topup, dispatch, isTopup, serviceCategory }: any) => {
  const [showAddProduct, setShowAddProduct] = useState(false);
  const [showDialog, setShowDialog] = useState(false);
  const [dataProvider, setProvider] = useState(new Array());
  const [isShowSpin, setLoading] = useState(true);
  const reqRef = useRef<number>(0);
  const useUserInfo = useRef<any>(null);
  const [mSaveProvider, setSaveProvider] = useState(new Array());
  const [product, setProduct] = useState(null);
  const [mRecord, setRecord] = useState<any>();
  const [mProvideName, setProviderName] = useState(undefined);

  const useEditProduct = useCallback(
    (record) => {
      setShowAddProduct(!showAddProduct);
      setProduct(record);
    },
    [setShowAddProduct, showAddProduct],
  );

  const useCloseView = useCallback(() => {
    setProduct(null);
    setShowAddProduct(false);
    dispatch({
      type: 'topup/getConfigTelco',
      params: {
        serviceCategory,
      },
    });
  }, [dispatch, serviceCategory]);

  const useNotiHangout = useCallback(
    (response, active, serviceId, price) => {
      const priceVND = `${price}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
      const { errorCode } = response || {};
      const userProfile = getDataLocal('login_successful');
      const { profileObj } = (userProfile as any) || {};
      const { email, name } = profileObj || {};
      const message =
        `//*${serviceId}*//: *${name}* (${email})` +
        ` đã ${active ? '*`OFF`*' : '*`ON`* '} Mệnh giá : *${priceVND}*` +
        ` - ${errorCode === 0 ? '`SUCCESS`' : '`ERROR`'}`;
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
    (record: any, serviceId: string, offProvider: boolean) => {
      setShowDialog(true);
      setRecord({ record, serviceId, offProvider });
    },
    [setRecord, setShowDialog],
  );

  const useCancel = useCallback(() => {
    setShowDialog(false);
  }, [setShowDialog]);

  const useCreate = useCallback(
    (offProvider, serviceId, record = [], description) => {
      if (description && dispatch) {
        setShowDialog(false);
        const { isDisableId } = record[0];
        const { profileObj } = useUserInfo.current || {};
        const { email = '' } = profileObj || {};

        if (offProvider !== null && serviceId) {
          fetch(environment() + API_TELCO.UPDATE_ON_OFF_PROVIDER, {
            method: 'POST',
            body: JSON.stringify({
              serviceId,
              isOffProvider: offProvider ? 1 : 0,
              username: email,
              description,
            }),
          })
            .then((response) => response.json())
            .then((responseJson) => {
              dispatch({
                type: 'topup/getConfigTelco',
                params: {
                  serviceCategory,
                },
              });
            })
            .catch((error) => {});
        } else {
          record.forEach((itemRecord: any) => {
            const { price, serviceId: serviceIdItemRecord, productId } = itemRecord || {};
            reqRef.current = requestAnimationFrame(() => {
              fetch(environment() + API_TELCO.POST_UPDATE_CONFIG, {
                method: 'POST',
                body: JSON.stringify({
                  serviceId: serviceIdItemRecord,
                  productId,
                  priceData: {
                    isDisableId: isDisableId === 0 ? 1 : 0,
                  },
                  username: email,
                  description,
                }),
              })
                .then((response) => response.json())
                .then((responseJson) => {
                  const { errorCode, errorDesc } = responseJson || {};
                  notification[errorCode === 0 ? 'success' : 'error']({
                    message: errorCode === 0 ? 'Cập nhật thành công!' : errorDesc,
                  });
                  useNotiHangout(responseJson, isDisableId === 0, serviceIdItemRecord, price);
                  dispatch({
                    type: 'topup/getConfigTelco',
                    params: {
                      serviceCategory,
                    },
                  });
                })
                .catch((error) => {});
            });
          });
        }
      }
    },
    [dispatch, serviceCategory, useNotiHangout],
  );

  const useData = useCallback(
    (topup: any) => {
      const { configTelco } = topup || {};
      const { data } = configTelco || {};
      const { listServiceEntity = [] } = data || {};
      const listProvider = Array();
      if (listServiceEntity) {
        listServiceEntity.forEach((item = {}, _index: number) => {
          const { serviceId, provider: providerName = '', isOffProvider, listPriceData = [] } =
            (item as any) || {};
          const provider = {} as any;
          provider.title = providerName;
          provider.bills = Array();
          provider.serviceId = serviceId;
          provider.isOffProvider = isOffProvider;
          let index = 0;
          if (listPriceData.length > 0) {
            listPriceData.sort((item1: any, item2: any) => {
              return Number(item1.value) - Number(item2.value);
            });
            listPriceData.forEach((element: any) => {
              const { value, warehouse, subStore, isDisableId, productId, extras, discount } =
                element || {};

              const bill = {} as any;
              // eslint-disable-next-line no-multi-assign
              bill.key = index += 1;
              bill.reference2 = providerName;
              bill.price = value;
              bill.serviceId = serviceId;
              bill.isDisableId = isDisableId;
              bill.productId = productId;
              bill.extras = extras;
              bill.discount = discount;
              bill.mainWarehouse = {
                name: warehouse || '',
                isMain: true,
              };
              bill.extraWarehouse = {
                name: subStore || '',
                isMain: false,
              };
              if (isDisableId === 0) {
                bill.active = true;
              } else {
                bill.active = false;
              }
              provider.bills.push(bill);
            });
            listProvider.push(provider);
            setSaveProvider(listProvider);
            if (mProvideName) {
              const main: any =
                listProvider.filter((ware: any) => ware.title === mProvideName) || [];
              setProvider(main.length === 0 ? listProvider : main);
            } else {
              setProvider(listProvider);
            }
            setLoading(false);
          }
        });
      }
    },
    [setLoading, setProvider, mProvideName],
  );

  const useChange = useCallback(
    (value: any) => {
      setProviderName(value);
      const main: any = mSaveProvider.filter((ware: any) => ware.title === value) || [];
      setProvider(main.length === 0 ? mSaveProvider : main);
    },
    [mSaveProvider, setProviderName],
  );

  useEffect(() => {
    // get profile
    useUserInfo.current = getDataLocal('login_successful');

    if (dispatch) {
      reqRef.current = requestAnimationFrame(() => {
        dispatch({
          type: 'topup/getConfigTelco',
          params: {
            serviceCategory,
          },
        });
      });
    }
    return () => {
      cancelAnimationFrame(reqRef.current);
      setProvider([]);
    };
  }, [dispatch, isTopup, reqRef, serviceCategory, setProvider]);

  useLayoutEffect(() => {
    useData(topup);
    return () => {};
  }, [topup, useData]);

  return (
    <>
      <div>
        <Spin spinning={isShowSpin}>
          <List
            grid={{ gutter: 50, column: 1 }}
            dataSource={dataProvider}
            header={
              <Select
                style={{ width: 500 }}
                showSearch
                allowClear
                placeholder="Search..."
                onChange={useChange}
                // onSearch={onSearch}
                optionFilterProp="children"
                filterOption={(input, option: any) =>
                  option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                }
                value={mProvideName}
              >
                {mSaveProvider.map((item: any) => (
                  <Option key={item.title} value={item.title}>
                    {item.title}
                  </Option>
                ))}
              </Select>
            }
            renderItem={(item: any) => (
              <ItemProvider
                data={item}
                useSwitchChange={useSwitchChange}
                useEditProduct={useEditProduct}
                onCloseView={useCloseView}
              />
            )}
          />
          <DrawerProduct product={product} visible={showAddProduct} onCloseView={useCloseView} />
          <CollectionCreateForm
            visible={showDialog}
            recordAll={mRecord}
            onCancel={useCancel}
            onCreate={useCreate}
          />
        </Spin>
      </div>
    </>
  );
};

export default connect(
  ({
    topup,
    warehouse,
    loading,
  }: {
    topup: any;
    warehouse: any;
    loading: {
      effects: { [key: string]: boolean };
    };
  }) => ({
    topup,
    warehouse,
    loading: loading.effects['topup/getTopupOld'],
  }),
)(Provider);
