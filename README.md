godlibrary-retrofit
======================
[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)


# 简介
1. 基于 retrofit2 的网络请求封装 经过项目 实战 验证。

# dependencies

```html
dependencies {
    compile 'com.android.support:appcompat-v7:24.2.1'
    compile 'io.reactivex:rxjava:1.2.2'
    compile 'io.reactivex:rxandroid:1.2.1'
    compile 'com.squareup.retrofit2:retrofit:2.1.0'
    compile 'com.squareup.retrofit2:adapter-rxjava:2.1.0'
    compile 'com.squareup.retrofit2:converter-gson:2.1.0'
}
```

# 使用方法

## 引用
```java
compile 'com.abook23:godlibrary-retrofit:1.0.1'
```
## Stop2
```java

/**
 * Created by abook23 on 2016-8-30.
 * E-mail abook23@163.com
 */
public interface UserApi {

    @POST(ServiceURL.login)
    @FormUrlEncoded
    Observable<RootBean<UserInfo>> login(
            @Field("userName") String userName,
            @Field("password") String password
    );
}


public interface UserService {
    /**
     * 登录
     *
     * @param userName 用户名
     * @param password 密码
     * @param call     回调
     */
    void login(String userName, String password, Call<RootBean> call);
}


public class UserServiceImpl extends BaseService implements UserService {

    private UserApi userApi = ApiService.create(UserApi.class);

    @Override
    public void login(final String userName, final String password, final Call<RootBean> call) {
        userApi.login(userName, password).map(new Func1<RootBean<UserInfo>, RootBean<UserInfo>>() {
            @Override
            public RootBean<UserInfo> call(RootBean<UserInfo> userInfoRootBean) {
                if (userInfoRootBean.isSucceed()) {
                    UserInfo localUserInfo = userInfoRootBean.getContent();
                    // code ------
                }
                return userInfoRootBean;
            }
        }).compose(RxJavaUtils.<RootBean<UserInfo>>defaultSchedulers()).subscribe(new WebObserver<RootBean<UserInfo>>() {
            @Override
            protected void onSuccess(RootBean<UserInfo> userInfoRootBean) {
                call.onSuccess(userInfoRootBean);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                call.onError(e);
            }
        });
    }

    public abstract class WebObserver<T> extends ObserverBaseWeb<T> {

        @Override
        public void onNext(T t) {
            if (t instanceof RootBean) {
                RootBean response = (RootBean) t;
                if (response.getState() == -1) {//未登陆 或者登陆超时

                    return;
                }
            }
            onSuccess(t);
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }
        protected abstract void onSuccess(T t);
    }

```
## 示例
```java
//PhotoActivity.CHECK_BUTTON_COLOR = R.color.;//选择按钮颜色
//PhotoActivity.COLOR_BACK_BUTTON = R.color.;// 顶部背景颜色
PhotoActivity.startActivityForResult(this, 9, null, 0)


@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                ArrayList<String> list = data.getStringArrayListExtra(PhotoActivity.DATA);
                if (list.size() == 0) {
                    return;
                }
                StringBuffer sb = new StringBuffer();
                for (String s : list) {
                    sb.append("\n").append(s);
                }
                L.d(sb.toString());
            }
        }
    }


public class LoginActivity extends Activity {

    private void LoginClick() {
        userService.login(loginName, pwd, new Call<RootBean>() {
            @Override
            public void onSuccess(RootBean rootBean) {
                Toast.makeText(context, rootBean.getMsg(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }
}
```
License
-------

    Copyright 2017 Wasabeef

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.