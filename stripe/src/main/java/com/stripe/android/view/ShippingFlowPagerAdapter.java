package com.stripe.android.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stripe.android.PaymentConfiguration;
import com.stripe.android.R;

import java.util.ArrayList;
import java.util.List;

class ShippingFlowPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ShippingFlowConfig mShippingFlowConfig;
    private List<ShippingFlowPagerEnum> mPages;


    ShippingFlowPagerAdapter(Context context, ShippingFlowConfig shippingFlowConfig) {
        mContext = context;
        mShippingFlowConfig = shippingFlowConfig;
        mPages = new ArrayList<>();
        if (!mShippingFlowConfig.hideAddressScreen()) {
            mPages.add(ShippingFlowPagerEnum.ADDRESS);
        }
        if (!mShippingFlowConfig.hideShippingScreen()) {
            mPages.add(ShippingFlowPagerEnum.SHIPPING_METHOD);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ShippingFlowPagerEnum shippingFlowPagerEnum = mPages.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(shippingFlowPagerEnum.getLayoutResId(), collection, false);
        if (shippingFlowPagerEnum.equals(ShippingFlowPagerEnum.SHIPPING_METHOD)) {
            SelectShippingMethodWidget selectShippingMethodWidget = layout.findViewById(R.id.select_shipping_method_widget);
            selectShippingMethodWidget.setShippingMethods(PaymentConfiguration.getInstance().getShippingMethods());
        }
        if (shippingFlowPagerEnum.equals(ShippingFlowPagerEnum.ADDRESS)) {
            AddAddressWidget addAddressWidget = layout.findViewById(R.id.add_address_widget);
            addAddressWidget.setHiddenFields(mShippingFlowConfig.getHiddenAddressFields());
            addAddressWidget.setOptionalFields(mShippingFlowConfig.getOptionalAddressFields());
            addAddressWidget.populateAddress(mShippingFlowConfig.getPrepopulatedAddress());
        }

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mPages.size();
    }

    @Nullable ShippingFlowPagerEnum getPageAt(int position) {
        if (position < mPages.size()) {
            return mPages.get(position);
        }
        return null;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(mPages.get(position).getTitleResId());
    }
}
