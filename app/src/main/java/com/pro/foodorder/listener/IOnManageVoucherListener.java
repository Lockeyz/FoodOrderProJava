package com.pro.foodorder.listener;

import com.pro.foodorder.model.Voucher;

public interface IOnManageVoucherListener {

    void onClickUpdateVoucher(Voucher Voucher);
    void onClickDeleteVoucher(Voucher voucher);
    void onClickExchangeVoucher(Voucher voucher);

}
