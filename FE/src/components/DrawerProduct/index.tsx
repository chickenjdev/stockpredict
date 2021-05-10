/* eslint-disable @typescript-eslint/no-shadow */
/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable react-hooks/rules-of-hooks */
/* eslint-disable @typescript-eslint/no-use-before-define */
import type { FC } from 'react';
import React, { useState, useEffect, useRef, useLayoutEffect, useCallback } from 'react';
import 'antd/dist/antd.css';
import './drawer.css';
import {
  Drawer,
  Form,
  Button,
  Col,
  Row,
  Input,
  Select,
  InputNumber,
  Spin,
  Result,
  notification,
  Switch,
} from 'antd';
import { connect, useIntl } from 'umi';
import { API_TELCO } from '@/utils/constants';
import { getDataLocal } from '@/utils/utils';
import { environment } from '@/utils/MServiceRequest';

const { Option } = Select;
interface WarehouseProps {
  loading: boolean;
  dispatch: Dispatch<any>;
  warehouse: any;
  visible: boolean;
  onCloseView: Function;
  product: any;
}

const DrawerProduct: FC<WarehouseProps> = ({
  product,
  visible,
  onCloseView,
  dispatch,
  warehouse,
}: WarehouseProps) => {
  const reqRef = useRef<number>(0);
  const [isShow, setVisible] = useState(false);
  const [mWarehouse, setWarehouse] = useState([]);
  const [spinning, setSpinning] = useState(false);
  const [isDone, setDone] = useState(false);
  const [form] = Form.useForm();
  const intl = useIntl();
  const useUserInfo = useRef<any>(null);
  const description = useRef<any>(null);

  const {
    reference2,
    price,
    serviceId,
    isDisableId,
    discount,
    productId,
    mainWarehouse,
    extraWarehouse,
  } = product || {};

  const { name: nameMain = '' } = mainWarehouse || {};
  const { name: nameExtra = '' } = extraWarehouse || {};
  const [warehouseMain, setWarehouseMain] = useState<any>({});
  const [warehouseExtra, setWarehouseExtra] = useState<any>({});
  const [checkedMain, setCheckedMain] = useState(false);
  const [checkedExtra, setCheckedExtra] = useState(false);
  const [activeProduct, setActiveProduct] = useState<boolean>(false);

  const useWarehouse = useCallback(
    (dataWarehouse) => {
      const { warehouseList } = dataWarehouse || {};
      const { data } = warehouseList || {};
      let { listWarehouse = [] } = data || {};
      if (listWarehouse == null) {
        listWarehouse = [];
      }
      const main = listWarehouse.find((ware: any) => ware.warehouse === nameMain) || [];
      const extra = listWarehouse.find((ware: any) => ware.warehouse === nameExtra) || [];
      setCheckedMain(main && main.status === 1);
      setCheckedExtra(extra && extra.status === 1);

      setWarehouse(listWarehouse || []);
      setWarehouseMain(main);
      setWarehouseExtra(extra);

      setSpinning(false);
    },
    [nameMain, nameExtra],
  );

  const useNotiHangout = useCallback(
    (response, serviceId, price) => {
      const priceVND = `${price}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
      const { errorCode } = response || {};
      const userProfile = getDataLocal('login_successful');
      const { profileObj } = (userProfile as any) || {};
      const { email, name } = profileObj || {};

      const { warehouse: warehouseNameMain = '' } = warehouseMain || {};
      const { warehouse: warehouseNameExtra = '' } = warehouseExtra || {};

      const message =
        `//*${serviceId}*//: *${name}* (${email})` +
        ` đã chuyển kho mệnh giá *${priceVND}* ` +
        `\n${
          checkedMain
            ? `từ kho *${warehouseNameExtra}* sang kho *${warehouseNameMain}*`
            : `từ kho *${warehouseNameMain}* sang kho *${warehouseNameExtra}*`
        } ` +
        ` \n- ${errorCode === 0 ? '`SUCCESS`' : '`ERROR`'}`;
      if (dispatch) {
        dispatch({
          type: 'topup/alertHangout',
          params: message,
        });
      }
    },
    [warehouseMain, warehouseExtra, checkedMain, dispatch],
  );

  const onFinish = useCallback(() => {
    setSpinning(true);
    if (dispatch && serviceId && description.current) {
      reqRef.current = requestAnimationFrame(() => {
        const { warehouse: warehouseNameMain = '' } = warehouseMain || {};
        const { warehouse: warehouseNameExtra = '' } = warehouseExtra || {};
        const { profileObj } = useUserInfo.current || {};
        const { email = '' } = profileObj || {};
        fetch(environment() + API_TELCO.POST_UPDATE_CONFIG, {
          method: 'POST',
          body: JSON.stringify({
            serviceId,
            productId,
            priceData: {
              warehouse: warehouseNameMain,
              subStore: warehouseNameExtra,
              price,
            },
            username: email,
            description: description.current,
          }),
        })
          .then((response) => response.json())
          .then((responseJson) => {
            setSpinning(false);
            const { errorCode, errorDesc } = responseJson || {};
            notification[errorCode === 0 ? 'success' : 'error']({
              message: errorCode === 0 ? 'Cập nhật thành công!' : errorDesc,
            });
            // disable noti hangout
            //useNotiHangout(responseJson, serviceId, price);
            setDone(errorCode === 0);

            // if (!checkedMain && !checkedExtra) {
            //   fetch(API_TELCO.POST_UPDATE_CONFIG, {
            //     method: 'POST',
            //     body: JSON.stringify({
            //       serviceId,
            //       productId,
            //       priceData: {
            //         isDisableId: isDisableId === 0 ? 1 : 0,
            //       },
            //       username: email,
            //       description,
            //     }),
            //   })
            //     .then((response) => response.json())
            //     .then((responseJson) => {
            //       const { errorCode, errorDesc } = responseJson || {};
            //       notification[errorCode === 0 ? 'success' : 'error']({
            //         message: errorCode === 0 ? 'Cập nhật thành công!' : errorDesc,
            //       });
            //       useNotiHangout(responseJson, active, serviceId, price);
            //     })
            //     .catch((error) => {});
            // }
          })
          .catch((_error) => {
            setSpinning(false);
          });
      });
    }
  }, [dispatch, price, productId, serviceId, useNotiHangout, warehouseExtra, warehouseMain]);

  const useChangeMain = useCallback(
    (value) => {
      const main: any = mWarehouse.find((ware: any) => ware.warehouse === value) || [];
      setCheckedMain(main.status === 1);
      setWarehouseMain(main || {});
    },
    [mWarehouse],
  );

  const useSwitchMain = (value: boolean) => {
    const extra = warehouseMain;
    extra.status = value ? 1 : 0;
    setWarehouseMain(extra);
    setCheckedMain(value);
  };

  const useChangeExtra = useCallback(
    (value) => {
      const extra: any = mWarehouse.find((ware: any) => ware.warehouse === value) || {};
      setCheckedExtra(extra.status === 1);
      setWarehouseExtra(extra || {});
    },
    [mWarehouse],
  );

  const useSwitchExtra = useCallback(
    (value: boolean) => {
      const extra = warehouseExtra;
      extra.status = value ? 1 : 0;
      setWarehouseExtra(extra);
      setCheckedExtra(value);
    },
    [warehouseExtra],
  );

  const useSwitchActive = useCallback(
    (value: boolean) => {
      setActiveProduct(value);
    },
    [setActiveProduct],
  );

  const onSearch = (val: any) => {
    console.log('search:', val);
  };

  const useChange = useCallback((value: any) => {
    description.current = value.target.value;
  }, []);

  const onClose = useCallback(() => {
    setVisible(false);
    setDone(false);
    onCloseView();
    form.resetFields();
  }, [form, onCloseView]);

  useEffect(() => {
    setVisible(visible);
    setActiveProduct(isDisableId === 0);
    setSpinning(false);
    // get profile
    useUserInfo.current = getDataLocal('login_successful');
    if (dispatch && serviceId) {
      reqRef.current = requestAnimationFrame(() => {
        dispatch({
          type: 'warehouse/fetchWarehouseList',
          params: {
            serviceId,
          },
        });
      });
    }
    return () => {
      cancelAnimationFrame(reqRef.current);
    };
  }, [dispatch, isDisableId, serviceId, setVisible, visible]);

  useLayoutEffect(() => {
    setSpinning(true);
    useWarehouse(warehouse);
    return () => {};
  }, [useWarehouse, warehouse]);

  return (
    <>
      <Drawer
        title={`Nhà mạng : ${reference2}`}
        width={720}
        onClose={onClose}
        visible={isShow}
        bodyStyle={{ paddingBottom: 80 }}
        footer={
          <div
            style={{
              textAlign: 'right',
            }}
          >
            <Button onClick={onClose} style={{ marginRight: 8 }}>
              {isDone ? 'Close' : 'Cancel'}
            </Button>
            {!isDone ? (
              <Button type="primary" form="myFormProduct" htmlType="submit">
                Submit
              </Button>
            ) : null}
          </div>
        }
      >
        {isDone ? (
          <Result status="success" title="Cập nhật thành công!" extra={[]} />
        ) : (
          <Spin size="large" spinning={spinning}>
            <Form
              id="myFormProduct"
              form={form}
              layout="vertical"
              name="control-hooks"
              requiredMark={true}
              onFinish={onFinish}
            >
              <Row gutter={16}>
                <Col span={12}>
                  <Form.Item
                    name={intl.formatMessage({
                      id: 'pages.provider.price',
                      defaultMessage: 'Mệnh giá',
                    })}
                    label={intl.formatMessage({
                      id: 'pages.provider.price',
                      defaultMessage: 'Mệnh giá',
                    })}
                    rules={[{ required: false, message: 'Vui lòng nhập mệnh giá' }]}
                    initialValue={price || ''}
                    valuePropName={'price'}
                  >
                    <InputNumber
                      style={{ width: '100%' }}
                      formatter={(value) => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
                      parser={(value) => (value && value.replace(/\$\s?|(,*)/g, '')) || ''}
                      placeholder="Vui lòng nhập mệnh giá"
                      value={price}
                      // defaultValue={price}
                      readOnly={true}
                    />
                  </Form.Item>
                </Col>
              </Row>
              <Row gutter={16}>
                <Col span={12}>
                  <Form.Item
                    name={intl.formatMessage({
                      id: 'pages.provider.mainWarehouse',
                      defaultMessage: 'Ưu tiên 1',
                    })}
                    label={intl.formatMessage({
                      id: 'pages.provider.mainWarehouse',
                      defaultMessage: 'Ưu tiên 1',
                    })}
                    rules={[{ required: true, message: 'Vui lòng nhập Ưu tiên 1' }]}
                    initialValue={'warehouseMain.warehouse'}
                    valuePropName={'mainwarehouse'}
                  >
                    <Select
                      showSearch
                      placeholder="Vui lòng nhập Ưu tiên 1"
                      value={warehouseMain.warehouse || ''}
                      onChange={useChangeMain}
                      onSearch={onSearch}
                      optionFilterProp="children"
                      filterOption={(input, option: any) =>
                        option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                      }
                    >
                      {mWarehouse
                        .filter((word: any) => word.warehouse !== warehouseExtra.warehouse)
                        .map((item: any) => (
                          <Option key={item.warehouse} value={item.warehouse}>
                            {item.warehouse}
                          </Option>
                        ))}
                    </Select>
                  </Form.Item>
                </Col>
                <Col span={12}>
                  <Form.Item
                    name={intl.formatMessage({
                      id: 'pages.provider.active',
                      defaultMessage: 'Active',
                    })}
                    label={intl.formatMessage({
                      id: 'pages.provider.active',
                      defaultMessage: 'Active',
                    })}
                  >
                    <Switch
                      key={Math.floor(Math.random() * 100)}
                      checkedChildren={'ON'}
                      unCheckedChildren={'OFF'}
                      checked={checkedMain}
                      onChange={useSwitchMain}
                    />
                  </Form.Item>
                </Col>
              </Row>
              <Row gutter={16}>
                <Col span={12}>
                  <Form.Item
                    name={intl.formatMessage({
                      id: 'pages.provider.extraWarehouse',
                      defaultMessage: 'Ưu tiên 2',
                    })}
                    label={intl.formatMessage({
                      id: 'pages.provider.extraWarehouse',
                      defaultMessage: 'Ưu tiên 2',
                    })}
                    initialValue={warehouseExtra.warehouse || ''}
                    valuePropName={'extrawarehouse'}
                  >
                    <Select
                      placeholder="Vui lòng nhập Ưu tiên 2"
                      value={warehouseExtra.warehouse || ''}
                      onChange={useChangeExtra}
                      allowClear
                    >
                      {mWarehouse
                        .filter((word: any) => word.warehouse !== warehouseMain.warehouse)
                        .map((item: any) => (
                          <Option key={item.warehouse} value={item.warehouse}>
                            {item.warehouse}
                          </Option>
                        ))}
                    </Select>
                  </Form.Item>
                </Col>
                <Col span={12}>
                  <Form.Item
                    name={intl.formatMessage({
                      id: 'pages.provider.active',
                      defaultMessage: 'Active',
                    })}
                    label={intl.formatMessage({
                      id: 'pages.provider.active',
                      defaultMessage: 'Active',
                    })}
                  >
                    <Switch
                      key={Math.floor(Math.random() * 100)}
                      checkedChildren={'ON'}
                      unCheckedChildren={'OFF'}
                      checked={checkedExtra}
                      onChange={useSwitchExtra}
                    />
                  </Form.Item>
                </Col>
              </Row>
              <Col span={16}>
                <Form.Item
                  name={intl.formatMessage({
                    id: 'pages.provider.active',
                    defaultMessage: 'Active',
                  })}
                  label={intl.formatMessage({
                    id: 'pages.provider.active',
                    defaultMessage: 'Active',
                  })}
                >
                  <Switch
                    key={Math.floor(Math.random() * 100)}
                    checkedChildren={'ON'}
                    unCheckedChildren={'OFF'}
                    checked={activeProduct}
                    onChange={useSwitchActive}
                  />
                </Form.Item>
              </Col>
              <Row gutter={16}>
                <Col span={24}>
                  <Form.Item
                    name="description"
                    label="Description"
                    rules={[
                      {
                        required: true,
                        message: 'Please enter description!',
                      },
                    ]}
                  >
                    <Input.TextArea onChange={useChange} rows={4} placeholder="Ghi chú..." />
                  </Form.Item>
                </Col>
              </Row>
            </Form>
          </Spin>
        )}
      </Drawer>
    </>
  );
};

export default connect(
  ({
    warehouse,
    loading,
  }: {
    warehouse: any;
    loading: {
      effects: { [key: string]: boolean };
    };
  }) => ({
    warehouse,
    loading: loading.effects['warehouse/fetchWarehouseList'],
  }),
)(DrawerProduct);
