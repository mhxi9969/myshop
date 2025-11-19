# 1 要件定義

------

## 1.1 背景・目的

 個人学習として EC サイトをフロント・バックエンド・インフラまで一貫して開発し、マイクロサービスの理解・実装スキルの習得を目的とする。 

------

## 1.2 業務要件

-   ユーザーがアカウントを登録できる

-   ユーザーがアカウントをログインできる

-   ユーザーが商品を検索できる

-   ユーザーが商品を閲覧できる

-   ユーザーが商品をカートに追加・削除

-   ユーザーが注文を作成できる

-   ユーザーが注文を決済できる

-   ユーザーが注文履歴を確認できる

-   ユーザーが注文詳細を確認できる

-   管理者がブランド管理できる

-   管理者がカテゴリ管理できる

-   管理者が商品管理できる

------


## 1.3 機能要件

-   ユーザー機能
    -   登録・ログイン API
    -   ユーザー情報取得

-   商品機能
    -   Elasticsearch による検索
    -   商品詳細 API
    -   画像取得（AWS S3）

-   カート機能
    -   追加 / 削除 / 数量変更 API
    -   Redis キャッシュ

-   注文機能
    -   注文作成 API
    -   在庫チェック
    -   RabbitMQ による非同期通知
    -   在庫ロルーバック

-   決済機能
    -   PayPay 決済リクエスト
    -   ステータス更新

-   注文履歴
    -   注文履歴一覧
    -   注文詳細

-   管理者商品管理
    -   登録 / 編集 / 削除
    -   画像アップロード（S3）
    -   管理者権限チェック

-   システム共通
    -   Spring Cloud Gateway ルーティング

    -   Spring Cloud Eureka サービスディスカバリ

------

## 1.4 非機能要件

-   性能：高頻度アクセスを想定し、Redis キャッシュでレスポンス高速化
-   可用性：複数サービス構成によるサービス分離
-   セキュリティ：Spring Security認証フィルター
-   保守性：サービスごとに機能を分離し、疎結合を維持
-   デプロイ性：Jenkins による自動ビルド & 自動デプロイ（CI/CD）

------

## 1.5 対象範囲

-   対象：フロント（Vue/Nuxt）、バックエンド（Spring Boot 10サービス）、インフラ（AWS）
-   対象外：実際の配送処理・本物の決済処理（PayPay Sandbox のみ）

------

## 1.6 前提条件・制約条件

-   Spring Boot / Spring Cloud を使用して開発
-   サービス間通信は REST API
-   クラウド環境は AWS（EC2 / S3 ）
-   インフラ構築は Docker + Jenkins

------

# 2 基本設計

------

## 2.1 システム構成

- フロントエンド：Vue / Nuxt
- バックエンド：Spring Boot 10 サービス
  - ユーザーサービス
  - 商品サービス
  - カートサービス
  - 注文サービス
  - 決済サービス
  - fileサービス
  - gatewayサービス
  - 検索サービス
  - eurekaサービス
  - 共通ユーティリティ
- データベース：MySQL
- キャッシュ：Redis
- メッセージキュー：RabbitMQ
- 検索：ElasticSearch
- ストレージ：S3
- ルーティング：Gateway
- サービスディスカバリ：Eureka
- CI/CD：Jenkins + Docker

------

## 2.2 画面基本設計

### 2.2.1 ユーザー画面

-   ホーム画面
-   新規登録画面
-   ログイン画面
-   商品検索画面
-   商品詳細画面
-   カート画面
-   注文作成画面
-   支払い画面
-   注文履歴画面
-   注文詳細画面

### 2.2.2 管理者画面

-   管理者ホーム画面
-   ブランド管理画面
-   カテゴリ管理画面
-   商品管理

### 2.2.3 共通画面

-   ユーザー画面レイアウト 

-   管理者画面レイアウト 

------

## 2.3 API基本設計

### 2.3.1 商品サービス

AttrController

-   DELETE  /product/attr/{id}   IDに基づいてAttrを削除する
-   GET  /product/attr/{id}  IDに基づいてAttrを取得する
-   GET  /product/attr/selectAllTree/{id}  二次カテゴリIDに基づき、すべての複雑なAttrを属性値オプション付きで取得する
-   GET  /product/attr/selectAll/{id}  二次カテゴリIDに基づき、すべてのAttrを取得する
-   POST  /product/attr  Attrを追加する
-   PUT  /product/attr  Attrを更新する

AttrValueController

-   DELETE  /product/attrValue/{id}  IDに基づいてAttrValueを削除する
-   GET  /product/attrValue/{id}  IDに基づいてAttrValueを取得する
-   GET  /product/attrValue/selectAll/{id}  指定されたAttr IDに関連するすべてのAttrValueを取得する
-   POST  /product/attrValue  AttrValueを追加する
-   PUT  /product/attrValue  AttrValueを更新する

BrandController

-   DELETE  /product/brand/{id}  IDに基づいてBrandを削除する
-   GET  /product/brand/{id}  IDに基づいてBrandを取得する
-   GET  /product/brand/selectAll  すべてのBrandを取得する
-   POST  /product/brand  Brandを追加する
-   POST  /product/brand/selectByCondition/{current}  条件に基づきBrandを検索、ページネーション対応
-   PUT  /product/brand  Brandを更新する

CategoryController

-   DELETE  /product/category/{id}  IDに基づいてCategoryを削除する
-   GET  /product/category/{id}  IDに基づいてCategoryを取得する
-   GET  /product/category/selectAll  すべての1級Categoryを取得する
-   GET  /product/category/selectAll2/{id}  親Category IDに基づきすべての2級Categoryを取得する
-   GET  /product/category/all2  すべての2級Categoryを取得する
-   POST  /product/category  Categoryを追加する
-   PUT  /product/category  Categoryを更新する

ProductSkuController

-   DELETE  /product/productSku/{id}  IDに基づいてProductSkuを削除する
-   GET  /product/productSku/{id}  IDに基づいて簡易ProductSkuを取得する
-   GET  /product/productSku/updateStock/{id}/{num}  ProductSkuの在庫を更新する
-   GET  /product/productSku/tree/{id}  IDに基づいて複雑なProductSkuを取得する
-   GET  /product/productSku/selectAllTreeBySpuId/{id}  SPU IDに基づきすべての複雑なProductSkuを取得する
-   GET  /product/productSku/rollBackStock/{id}/{num}  在庫をロールバックする
-   POST  /product/productSku  複雑なProductSkuを追加する
-   POST  /product/productSku/selectByCondition/{current}  条件に基づき簡易Productをページング取得する
-   PUT  /product/productSku  複雑なProductSkuを更新する

ProductSpuController

-   DELETE  /product/productSpu/{id}  IDに基づいてProductSpuを削除する
-   GET  /product/productSpu/{id}  IDに基づいてProductSpuを取得する
-   GET  /product/productSpu/selectAll  すべてのProductSpuを取得する
-   POST  /product/productSpu  ProductSpuを追加する
-   POST  /product/productSpu/selectByCondition/{current}  条件に基づきProductSpuをページング取得する
-   PUT  /product/productSpu  ProductSpuを更新する

SkuAttrValueController

-   DELETE  /product/skuAttrValue/{id}  IDに基づいてSkuAttrValueを削除する
-   GET  /product/skuAttrValue/{id}  IDに基づいてSkuAttrValueを取得する
-   GET  /product/skuAttrValue/selectAll  すべてのSkuAttrValueを取得する
-   POST  /product/skuAttrValue  SkuAttrValueを追加する
-   PUT  /product/skuAttrValue  SkuAttrValueを更新する

### 2.3.2 ユーザーサービス

UserController

-   DELETE  /user/user/{id}  IDに基づいてUserを削除する
-   GET  /user/user/{id}  IDに基づいてUserを取得する
-   GET  /user/user/selectAll  すべてのUserを取得する
-   POST  /user/user  Userを追加する
-   POST  /user/user/sendCode  認証コードを送信する
-   POST  /user/user/register  ユーザー登録
-   POST  /user/user/logout  ユーザーをログアウトさせる
-   POST  /user/user/login  ユーザーログイン
-   POST  /user/user/getUserBySession   セッションからユーザー情報を取得
-   PUT  /user/user  User情報を更新

### 2.3.3 fileサービス

FileController

-   POST  /file/file/upload  ファイルをアップロードする

### 2.3.4 検索サービス

SearchController

-   POST  /search/search/searchBySpuID/{id}  SPU IDに基づいて検索を実行する
-   POST  /search/search/search/{page}  ページ番号に基づき検索を実行

-   POST  /search/search/remove  検索インデックスからデータを削除

-   POST  /search/search/add  検索インデックスにデータを追加

### 2.3.5 カートサービス

CartController

-   DELETE  /cart/cart/{id}  IDに基づいてカートアイテムを削除
-   GET  /cart/cart/selectAll  すべてのカートアイテムを取得
-   GET  /cart/cart/selectAllChecked  選択済みのカートアイテムを取得
-   POST  /cart/cart  カートアイテムを追加
-   PUT  /cart/cart  カートアイテムを更新

### 2.3.6 注文サービス

OrderController

-   DELETE  /order/order/{id}  IDに基づいて注文を削除
-   GET  /order/order/{id}  IDに基づいて注文を取得
-   GET  /order/order/token  決済ページ進入時にトークンを生成して二重注文を防止
-   GET  /order/order/selectAll  すべての注文を取得
-   GET  /order/order/poll/{id}  注文状態をポーリングで更新
-   POST  /order/order  注文を追加
-   PUT  /order/order  注文を更新

OrderItemController

-   DELETE  /order/orderItem/{id}  IDに基づいて注文アイテムを削除
-   GET  /order/orderItem/{id}  IDに基づいて注文アイテムを取得
-   GET  /order/orderItem/selectAll/{id}  指定Orderのすべての注文アイテムを取得
-   POST  /order/orderItem  注文アイテムを追加
-   PUT  /order/orderItem  注文アイテムを更新

### 2.3.7 決済サービス

PayController

-   
    GET  /pay/pay/search/{id}/  指定IDの支払い情報を取得

-   GET  /pay/pay/create/{id}/{price}  指定IDと金額で支払いを作成

------

## 2.4 データベース基本設計

### 2.4.1 ユーザーサービス

-   user_user：id，name，password，email，phone，role，create_time，update_time

### 2.4.2 商品サービス

-   product_attr：id, name, category_id, create_time, update_time
-   product_attr_value：id, name, attr_id, create_time, update_time
-   product_brand：id, name, picture, create_time, update_time
-   product_category：id, name, parent_id, sort, create_time, update_time
-   product_product_sku：id, name, spu_id, picture, price, stock, create_time, update_time
-   product_product_spu：id, name, brand_id, category1_id, category2_id, status, create_time, update_time
-   product_sku_attr_value：id, sku_id, attr_id, value_id, create_time, update_time

### 2.4.3 注文サービス

-   order_order：id, user_id, total_amount, status, receiver_name, receiver_phone, receiver_address, trade_id, 

    pay_time, delivery_time, receive_time, close_time, remark, create_time, update_time

-   order_order_item：id, order_id, sku_id, sku_name, sku_picture, sku_price, sku_quantity, total_price, 

    create_time, update_time

------

# 3 機能設計

------

## 3.1 画面遷移

- ユーザー画面
  - ホーム → 商品検索 → 商品詳細 → カート → 注文作成 → 支払い → 注文履歴 → 注文詳細
  - ログイン / 新規登録は任意の画面から遷移可能
- 管理者画面
  - 商品管理 → 商品登録 / 編集 / 削除

------

## 3.2 入力項目

### 3.2.1 ユーザー関連
- 名前、メール、電話番号、パスワード、住所

### 3.2.2 商品関連
- 商品名、ブランド、カテゴリー、価格、在庫、SKU属性

### 3.2.3 注文関連
- 受取人氏名、電話、住所、商品数量

------

## 3.3 業務フロー

検索キーワード入力

検索サービスに送信

該当商品を取得

フィルタリング

検索結果画面に表示

ユーザーがカートに商品を追加

カート画面で商品確認・数量調整

注文作成画面で配送情報入力

注文確定 → 注文テーブル作成

支払い処理（PayPay）

支払い成功 → 注文ステータス「支払い済」に更新

------


# 4 詳細設計

------

## 4.1 画面詳細設計

### 4.1.1 ユーザー画面

**ホーム画面**

表示内容

-   メインコンテンツ：輪播図（Carousel）のみ
-   ヘッダー・フッターは共通 layout に含まれる

操作

-   商品検索 → 商品検索結果画面に遷移
-   ログイン / 新規登録 → ログイン／新規登録画面に遷移

入力項目

-   検索キーワード

**新規登録画面**

-   入力項目
    -   名前（必須）
    -   メール（必須、メール形式）
    -   パスワード（必須、8文字以上）
-   操作
    
    -   登録 
-   遷移
    -   成功 → ホーム画面
    -   失敗 → エラーメッセージ表示
    
    

**ログイン画面**

-   入力項目
    -   名前
    -   パスワード
-   操作
    -   ログイン 
-   遷移
    -   成功 → ホーム画面
    -   失敗 → エラーメッセージ表示
-   補足
    -   セッション管理、ログイン状態保持

**商品検索画面**

-   表示内容: 検索結果一覧、フィルター
-   操作
    -   商品選択 → 商品詳細画面に遷移
-   入力項目
    -   検索キーワード
    -   フィルター条件

**商品詳細画面**

-   表示内容: 商品名、価格、SKU選択、商品画像、カート追加ボタン
-   操作
    -   カート追加
-   入力項目
    -   SKU選択
    -   購入数量

**カート画面**

-   表示内容: カート内商品一覧、合計金額、チェックボックス
-   操作
    -   商品数量変更 
    -   商品削除 
    -   注文作成 → 注文作成画面に遷移
    
    

**注文作成画面**

-   表示内容: 商品一覧、配送先入力
-   操作
    -   注文確定
-   入力項目
    -   配送先氏名、電話、住所
-   遷移
    -   成功 → 注文完了画面
    -   失敗 → エラーメッセージ表示

**支払い画面**

-   表示内容: 注文金額、支払いボタン、注文内容確認
-   操作
    -   支払い実行 → Paypay支払い画面
-   遷移
    -   支払い成功 → 注文履歴画面
    
        

**注文履歴画面**

-   表示内容: 過去注文一覧、注文ステータス
-   操作
    
    -   注文詳細 → 注文詳細画面に遷移
    
    

**注文詳細画面**

-   表示内容: 注文商品明細、合計金額、支払い状況
-   操作
    
    -   必要に応じてキャンセル・返品（条件付き）
    
    

------

### 4.1.2 管理者画面

**管理者ホーム画面**

-   表示内容: 管理ダッシュボード
-   操作
    -   商品管理画面、ブランド管理画面、カテゴリ管理画面への遷移
    -   注文管理画面への遷移

**ブランド管理画面**

-   表示内容: ブランド一覧
-   操作
    -   新規登録 
    -   編集 
    -   削除 

**カテゴリ管理画面**

-   表示内容: カテゴリ一覧（階層表示）
-   操作
    -   新規登録 
    -   編集 
    -   削除 

**商品管理画面**

-   表示内容: 商品一覧（SPU/SKU）
-   操作
    -   新規登録 
    -   編集 
    -   削除 

------

### 4.1.3 共通画面

**ユーザー画面レイアウト**

-   ナビゲーションバー（ホーム、カート、注文履歴、ログイン状態表示）
-   フッター（会社情報、問い合わせ）

    

**管理者画面レイアウト**

-   ナビゲーションバー（ダッシュボード、商品管理、ブランド管理、カテゴリ管理、注文管理）

    

------

## 4.2 API詳細設計

### 4.2.1 商品サービス

**Attr管理**

DELETE /product/attr/{id}

-   説明: IDに基づいてAttrを削除する
-   Controller: `AttrController.deleteAttr(id)`
-   Service手順:
    1.  Attrが存在するか確認
    2.  必要に応じてAttrに関連するAttrValueを削除
    3.  Attrレコードを削除
    4.  操作結果を返却
-   DB操作:
    -   確認: `SELECT * FROM product_attr WHERE id=?`
    -   関連値削除: `DELETE FROM product_attr_value WHERE attr_id=?`
    -   Attr削除: `DELETE FROM product_attr WHERE id=?`

GET /product/attr/{id}

-   説明: IDに基づいてAttrを取得する
-   Controller: `AttrController.getAttrById(id)`
-   Service手順:
    1.  Attr情報を取得
    2.  結果を返却
-   DB操作: `SELECT * FROM product_attr WHERE id=?`

GET /product/attr/selectAllTree/{id}

-   説明: 二次カテゴリIDに基づき、すべての複雑なAttrを属性値オプション付きで取得する
-   Controller: `AttrController.getAllTreeByCategoryId(id)`
-   Service手順:
    1.  該当カテゴリ下のすべてのAttrを取得
    2.  各AttrのAttrValueリストを取得
    3.  ツリー構造に組み立てて返却
-   DB操作:
    -   Attr取得: `SELECT * FROM product_attr WHERE category_id=?`
    -   AttrValue取得: `SELECT * FROM product_attr_value WHERE attr_id IN (...)`

GET /product/attr/selectAll/{id}

-   説明: 二次カテゴリIDに基づき、すべてのAttrを取得する
-   Controller: `AttrController.getAllByCategoryId(id)`
-   Service手順:
    1.  該当カテゴリ下のすべてのAttrを取得
    2.  リスト結果を返却
-   DB操作: `SELECT * FROM product_attr WHERE category_id=?`

POST /product/attr

-   説明: Attrを追加する
-   Controller: `AttrController.createAttr(attrDto)`
-   Service手順:
    1.  入力パラメータを検証（名前、カテゴリなど必須項目）
    2.  Attrレコードを挿入
    3.  属性値リストがあれば、AttrValueを一括挿入
    4.  操作結果を返却
-   DB操作:
    -   Attr挿入: `INSERT INTO product_attr (name, category_id, type, ...) VALUES (...)`
    -   AttrValue一括挿入: `INSERT INTO product_attr_value (attr_id, value, ...) VALUES (...)`

PUT /product/attr

-   説明: Attrを更新する
-   Controller: `AttrController.updateAttr(attrDto)`
-   Service手順:
    1.  Attrが存在するか確認
    2.  Attr情報を更新
    3.  関連AttrValueを更新または新規追加（渡されたデータに応じて）
    4.  操作結果を返却
-   DB操作:
    -   Attr更新: `UPDATE product_attr SET name=?, type=?, ... WHERE id=?`
    -   AttrValue更新: `UPDATE product_attr_value SET value=?, ... WHERE id=?`
    -   新規AttrValue追加（必要に応じて）: `INSERT INTO product_attr_value (attr_id, value, ...) VALUES (...)`

**AttrValue管理**

DELETE /product/attrValue/{id}

-   説明: IDに基づいてAttrValueを削除する
-   Controller: `AttrValueController.deleteAttrValue(id)`
-   Service手順:
    1.  AttrValueが存在するか確認
    2.  AttrValueを削除
    3.  操作結果を返却
-   DB操作: `DELETE FROM product_attr_value WHERE id=?`

GET /product/attrValue/{id}

-   説明: IDに基づいてAttrValueを取得する
-   Controller: `AttrValueController.getAttrValueById(id)`
-   Service手順:
    1.  AttrValue情報を取得
    2.  結果を返却
-   DB操作: `SELECT * FROM product_attr_value WHERE id=?`

GET /product/attrValue/selectAll/{id}

-   説明: 指定されたAttr IDに関連するすべてのAttrValueを取得する
-   Controller: `AttrValueController.getAllByAttrId(id)`
-   Service手順:
    1.  Attrに紐づくすべてのAttrValueを取得
    2.  リスト結果を返却
-   DB操作: `SELECT * FROM product_attr_value WHERE attr_id=?`

POST /product/attrValue

-   説明: AttrValueを追加する
-   Controller: `AttrValueController.createAttrValue(attrValueDto)`
-   Service手順:
    1.  入力パラメータを検証（値名、Attr IDなど必須項目）
    2.  AttrValueレコードを挿入
    3.  操作結果を返却
-   DB操作: `INSERT INTO product_attr_value (attr_id, value, ...) VALUES (...)`

PUT /product/attrValue

-   説明: AttrValueを更新する
-   Controller: `AttrValueController.updateAttrValue(attrValueDto)`
-   Service手順:
    1.  AttrValueが存在するか確認
    2.  AttrValue情報を更新
    3.  操作結果を返却
-   DB操作: `UPDATE product_attr_value SET value=?, ... WHERE id=?`

**Brand管理**

DELETE /product/brand/{id}

-   説明: IDに基づいてBrandを削除する
-   Controller: `BrandController.deleteBrand(id)`
-   Service手順:
    1.  Brandが存在するか確認
    2.  Brandを削除
    3.  操作結果を返却
-   DB操作: `DELETE FROM product_brand WHERE id=?`

GET /product/brand/{id}

-   説明: IDに基づいてBrandを取得する
-   Controller: `BrandController.getBrandById(id)`
-   Service手順:
    1.  Brand情報を取得
    2.  結果を返却
-   DB操作: `SELECT * FROM product_brand WHERE id=?`

GET /product/brand/selectAll

-   説明: すべてのBrandを取得する
-   Controller: `BrandController.getAllBrands()`
-   Service手順:
    1.  DBからすべてのBrandを取得
    2.  リストを返却
-   DB操作: `SELECT * FROM product_brand`

POST /product/brand

-   説明: Brandを追加する
-   Controller: `BrandController.createBrand(brandDto)`
-   Service手順:
    1.  入力バリデーション
    2.  DBにINSERT
    3.  結果を返却
-   DB操作: `INSERT INTO product_brand (...) VALUES (...)`

POST /product/brand/selectByCondition/{current}

-   説明: 条件に基づきBrandを検索、ページネーション対応
-   Controller: `BrandController.selectByCondition(brandQueryDto, current)`
-   Service手順:
    1.  検索条件を解析
    2.  DBから条件に一致するBrandを取得（LIMIT, OFFSETでページネーション）
    3.  結果を返却
-   DB操作: `SELECT * FROM product_brand WHERE ... LIMIT ?, ?`

PUT /product/brand

-   説明: Brandを更新する
-   Controller: `BrandController.updateBrand(brandDto)`
-   Service手順:
    1.  Brandが存在するか確認
    2.  DBでBrand情報を更新
    3.  結果を返却
-   DB操作: `UPDATE product_brand SET ... WHERE id=?`

**Category管理**

DELETE /product/category/{id}

-   説明: IDに基づいてCategoryを削除する
-   Controller: `CategoryController.deleteCategory(id)`
-   Service手順:
    1.  Categoryが存在するか確認
    2.  Categoryを削除
    3.  結果を返却
-   DB操作: `DELETE FROM product_category WHERE id=?`

GET /product/category/{id}

-   説明: IDに基づいてCategoryを取得する
-   Controller: `CategoryController.getCategoryById(id)`
-   Service手順:
    1.  DBからCategory情報を取得
    2.  結果を返却
-   DB操作: `SELECT * FROM product_category WHERE id=?`

GET /product/category/selectAll

-   説明: すべての1級Categoryを取得する
-   Controller: `CategoryController.getAllFirstLevelCategories()`
-   Service手順:
    1.  DBからすべての1級Categoryを取得
    2.  結果を返却
-   DB操作: `SELECT * FROM product_category WHERE parent_id IS NULL`

GET /product/category/selectAll2/{id}

-   説明: 親Category IDに基づきすべての2級Categoryを取得する
-   Controller: `CategoryController.getSecondLevelCategoriesByParentId(id)`
-   Service手順:
    1.  DBから指定の親IDの2級Categoryを取得
    2.  結果を返却
-   DB操作: `SELECT * FROM product_category WHERE parent_id=?`

GET /product/category/all2

-   説明: すべての2級Categoryを取得する
-   Controller: `CategoryController.getAllSecondLevelCategories()`
-   Service手順:
    1.  DBからすべての2級Categoryを取得
    2.  結果を返却
-   DB操作: `SELECT * FROM product_category WHERE parent_id IS NOT NULL`

POST /product/category

-   説明: Categoryを追加する
-   Controller: `CategoryController.createCategory(categoryDto)`
-   Service手順:
    1.  入力バリデーション
    2.  DBにINSERT
    3.  結果を返却
-   DB操作: `INSERT INTO product_category (...) VALUES (...)`

PUT /product/category

-   説明: Categoryを更新する
-   Controller: `CategoryController.updateCategory(categoryDto)`
-   Service手順:
    1.  Categoryが存在するか確認
    2.  DBでCategory情報を更新
    3.  結果を返却
-   DB操作: `UPDATE product_category SET ... WHERE id=?`

**ProductSku管理**

DELETE /product/productSku/{id}

-   説明: IDに基づいてProductSkuを削除する
-   Controller: `ProductSkuController.deleteProductSku(id)`
-   Service手順:
    1.  ProductSkuが存在するか確認
    2.  DBから削除
    3.  結果を返却
-   DB操作: `DELETE FROM product_sku WHERE id=?`

GET /product/productSku/{id}

-   説明: IDに基づいて簡易ProductSkuを取得する
-   Controller: `ProductSkuController.getProductSkuById(id)`
-   Service手順:
    1.  DBからProductSku情報を取得
    2.  結果を返却
-   DB操作: `SELECT * FROM product_sku WHERE id=?`

GET /product/productSku/updateStock/{id}/{num}

-   説明: ProductSkuの在庫を更新する
-   Controller: `ProductSkuController.updateStock(id, num)`
-   Service手順:
    1.  DBで在庫数量を更新
    2.  結果を返却
-   DB操作: `UPDATE product_sku SET stock=stock+? WHERE id=?`

GET /product/productSku/tree/{id}

-   説明: IDに基づいて複雑なProductSkuを取得する
-   Controller: `ProductSkuController.getProductSkuTreeById(id)`
-   Service手順:
    1.  DBからProductSkuと関連属性情報を取得
    2.  結果を返却
-   DB操作: `SELECT s.*, a.* FROM product_sku s LEFT JOIN sku_attr_value a ON s.id=a.sku_id WHERE s.id=?`

GET /product/productSku/selectAllTreeBySpuId/{id}

-   説明: SPU IDに基づきすべての複雑なProductSkuを取得する
-   Controller: `ProductSkuController.getAllSkuTreeBySpuId(id)`
-   Service手順:
    1.  DBからSPUに紐づくすべてのProductSkuと属性情報を取得
    2.  結果を返却
-   DB操作: `SELECT s.*, a.* FROM product_sku s LEFT JOIN sku_attr_value a ON s.id=a.sku_id WHERE s.spu_id=?`

GET /product/productSku/rollBackStock/{id}/{num}

-   説明: 在庫をロールバックする
-   Controller: `ProductSkuController.rollBackStock(id, num)`
-   Service手順:
    1.  DBで在庫数量をロールバック
    2.  結果を返却
-   DB操作: `UPDATE product_sku SET stock=stock+? WHERE id=?`

POST /product/productSku

-   説明: 複雑なProductSkuを追加する
-   Controller: `ProductSkuController.createProductSku(productSkuDto)`
-   Service手順:
    1.  入力バリデーション
    2.  DBにINSERT
    3.  関連SKU属性を登録
    4.  結果を返却
-   DB操作: `INSERT INTO product_sku (...) VALUES (...)`

POST /product/productSku/selectByCondition/{current}

-   説明: 条件に基づき簡易Productをページング取得する
-   Controller: `ProductSkuController.selectByCondition(conditionDto, current)`
-   Service手順:
    1.  DBで条件に一致するProductSkuを検索
    2.  ページング処理
    3.  結果を返却
-   DB操作: `SELECT * FROM product_sku WHERE ... LIMIT ?, ?`

PUT /product/productSku

-   説明: 複雑なProductSkuを更新する
-   Controller: `ProductSkuController.updateProductSku(productSkuDto)`
-   Service手順:
    1.  ProductSkuが存在するか確認
    2.  DBで情報を更新
    3.  関連SKU属性を更新
    4.  結果を返却
-   DB操作: `UPDATE product_sku SET ... WHERE id=?`

**ProductSpu管理**

DELETE /product/productSpu/{id}

-   説明: IDに基づいてProductSpuを削除する
-   Controller: `ProductSpuController.deleteProductSpu(id)`
-   Service手順:
    1.  ProductSpuの存在確認
    2.  DBから削除
    3.  結果を返却
-   DB操作: `DELETE FROM product_product_spu WHERE id=?`

GET /product/productSpu/{id}

-   説明: IDに基づいてProductSpuを取得する
-   Controller: `ProductSpuController.getProductSpuById(id)`
-   Service手順:
    1.  DBからProductSpu情報を取得
    2.  結果を返却
-   DB操作: `SELECT * FROM product_product_spu WHERE id=?`

GET /product/productSpu/selectAll

-   説明: すべてのProductSpuを取得する
-   Controller: `ProductSpuController.getAllProductSpu()`
-   Service手順:
    1.  DBからすべてのProductSpuを取得
    2.  結果を返却
-   DB操作: `SELECT * FROM product_product_spu`

POST /product/productSpu

-   説明: ProductSpuを追加する
-   Controller: `ProductSpuController.createProductSpu(productSpuDto)`
-   Service手順:
    1.  入力バリデーション
    2.  DBにINSERT
    3.  必要に応じて関連SKU登録
    4.  結果を返却
-   DB操作: `INSERT INTO product_product_spu (...) VALUES (...)`

POST /product/productSpu/selectByCondition/{current}

-   説明: 条件に基づきProductSpuをページング取得する
-   Controller: `ProductSpuController.selectByCondition(conditionDto, current)`
-   Service手順:
    1.  DBで条件に一致するProductSpuを検索
    2.  ページング処理
    3.  結果を返却
-   DB操作: `SELECT * FROM product_product_spu WHERE ... LIMIT ?, ?`

PUT /product/productSpu

-   説明: ProductSpuを更新する
-   Controller: `ProductSpuController.updateProductSpu(productSpuDto)`
-   Service手順:
    1.  ProductSpuの存在確認
    2.  DBで情報を更新
    3.  必要に応じて関連SKU属性を更新
    4.  結果を返却
-   DB操作: `UPDATE product_product_spu SET ... WHERE id=?`

**SkuAttrValue管理**

-   DELETE /product/skuAttrValue/{id}

    -   説明: IDに基づいてSkuAttrValueを削除する
    -   Controller: `SkuAttrValueController.deleteSkuAttrValue(id)`
    -   Service手順:
        1.  SkuAttrValueの存在確認
        2.  DBから削除
        3.  結果を返却
    -   DB操作: `DELETE FROM product_sku_attr_value WHERE id=?`

    GET /product/skuAttrValue/{id}

    -   説明: IDに基づいてSkuAttrValueを取得する
    -   Controller: `SkuAttrValueController.getSkuAttrValueById(id)`
    -   Service手順:
        1.  DBからSkuAttrValue情報を取得
        2.  結果を返却
    -   DB操作: `SELECT * FROM product_sku_attr_value WHERE id=?`

    GET /product/skuAttrValue/selectAll

    -   説明: すべてのSkuAttrValueを取得する
    -   Controller: `SkuAttrValueController.getAllSkuAttrValue()`
    -   Service手順:
        1.  DBからすべてのSkuAttrValueを取得
        2.  結果を返却
    -   DB操作: `SELECT * FROM product_sku_attr_value`

    POST /product/skuAttrValue

    -   説明: SkuAttrValueを追加する
    -   Controller: `SkuAttrValueController.createSkuAttrValue(skuAttrValueDto)`
    -   Service手順:
        1.  入力バリデーション
        2.  DBにINSERT
        3.  結果を返却
    -   DB操作: `INSERT INTO product_sku_attr_value (...) VALUES (...)`

    PUT /product/skuAttrValue

    -   説明: SkuAttrValueを更新する
    -   Controller: `SkuAttrValueController.updateSkuAttrValue(skuAttrValueDto)`
    -   Service手順:
        1.  SkuAttrValueの存在確認
        2.  DBで情報を更新
        3.  結果を返却
    -   DB操作: `UPDATE product_sku_attr_value SET ... WHERE id=?`

### 4.2.2 ユーザーサービス

**User管理**

DELETE /user/user/{id}

-   説明: IDに基づいてUserを削除する
-   Controller: `UserController.deleteUser(id)`
-   Service手順:
    1.  Userの存在確認
    2.  DBから削除
    3.  結果を返却
-   DB操作: `DELETE FROM user_user WHERE id=?`

GET /user/user/{id}

-   説明: IDに基づいてUserを取得する
-   Controller: `UserController.getUserById(id)`
-   Service手順:
    1.  DBからUser情報を取得
    2.  結果を返却
-   DB操作: `SELECT * FROM user_user WHERE id=?`

GET /user/user/selectAll

-   説明: すべてのUserを取得する
-   Controller: `UserController.getAllUsers()`
-   Service手順:
    1.  DBから全Userを取得
    2.  結果を返却
-   DB操作: `SELECT * FROM user_user`

POST /user/user

-   説明: Userを追加する
-   Controller: `UserController.createUser(userDto)`
-   Service手順:
    1.  入力バリデーション
    2.  DBにINSERT
    3.  結果を返却
-   DB操作: `INSERT INTO user_user (...) VALUES (...)`

POST /user/user/sendCode

-   説明: 認証コードを送信する
-   Controller: `UserController.sendVerificationCode(userDto)`
-   Service手順:
    1.  メールまたはSMS送信
    2.  送信結果を返却

POST /user/user/register

-   説明: ユーザー登録
-   Controller: `UserController.register(userDto)`
-   Service手順:
    1.  入力バリデーション
    2.  DBに新規Userを追加
    3.  結果を返却
-   DB操作: `INSERT INTO user_user (...) VALUES (...)`

POST /user/user/logout

-   説明: ユーザーをログアウトさせる
-   Controller: `UserController.logout(session)`
-   Service手順:
    1.  セッション削除または無効化
    2.  結果を返却

POST /user/user/login

-   説明: ユーザーログイン
-   Controller: `UserController.login(loginDto)`
-   Service手順:
    1.  ユーザー認証
    2.  セッションまたはトークン生成
    3.  結果を返却

POST /user/user/getUserBySession

-   説明: セッションからユーザー情報を取得
-   Controller: `UserController.getUserBySession(session)`
-   Service手順:
    1.  セッション確認
    2.  DBからUser情報取得
    3.  結果を返却

PUT /user/user

-   説明: User情報を更新
-   Controller: `UserController.updateUser(userDto)`
-   Service手順:
    1.  Userの存在確認
    2.  DB情報更新
    3.  結果を返却
-   DB操作: `UPDATE user_user SET ... WHERE id=?`

### 4.2.3 fileサービス

**File管理**

POST /file/file/upload

-   説明: ファイルをアップロードする
-   Controller: `FileController.uploadFile(file)`
-   Service手順:
    1.  ファイル形式とサイズのバリデーション
    2.  ストレージ（ローカル/クラウド）に保存
    3.  保存結果やURLを返却
-   DB操作（必要に応じて）: `INSERT INTO file_file (file_name, file_url, size, created_at) VALUES (...)`

### 4.2.4 検索サービス

**search管理**

POST /search/search/searchBySpuID/{id}

-   説明: SPU IDに基づいて検索を実行する
-   Controller: `SearchController.searchBySpuID(id)`
-   Service手順:
    1.  検索エンジンにクエリ送信
    2.  結果を取得
    3.  レスポンス返却
-   DB操作: 必要に応じて検索対象データ取得

POST /search/search/search/{page}

-   説明: ページ番号に基づき検索を実行
-   Controller: `SearchController.search(page, searchParams)`
-   Service手順:
    1.  検索条件のバリデーション
    2.  検索エンジンにクエリ送信
    3.  結果をページングして返却

POST /search/search/remove

-   説明: 検索インデックスからデータを削除
-   Controller: `SearchController.remove(searchData)`
-   Service手順:
    1.  削除対象データ確認
    2.  検索エンジンから削除
    3.  結果返却

POST /search/search/add

-   説明: 検索インデックスにデータを追加
-   Controller: `SearchController.add(searchData)`
-   Service手順:
    1.  追加データのバリデーション
    2.  検索エンジンに追加
    3.  結果返却

### 4.2.5 カートサービス

**Cart管理**

DELETE /cart/cart/{id}

-   説明: IDに基づいてカートアイテムを削除
-   Controller: `CartController.deleteCartItem(id)`
-   Service手順:
    1.  対象カートアイテムの存在確認
    2.  DBから削除
    3.  結果返却
-   DB操作: `DELETE FROM cart_cart WHERE id=?`

GET /cart/cart/selectAll

-   説明: すべてのカートアイテムを取得
-   Controller: `CartController.selectAll()`
-   Service手順:
    1.  DBからすべてのカートアイテム取得
    2.  レスポンス返却
-   DB操作: `SELECT * FROM cart_cart`

GET /cart/cart/selectAllChecked

-   説明: 選択済みのカートアイテムを取得
-   Controller: `CartController.selectAllChecked()`
-   Service手順:
    1.  DBから選択済みカートアイテム取得
    2.  レスポンス返却
-   DB操作: `SELECT * FROM cart_cart WHERE checked=1`

POST /cart/cart

-   説明: カートアイテムを追加
-   Controller: `CartController.addCartItem(cartItemDto)`
-   Service手順:
    1.  入力データのバリデーション
    2.  DBにINSERT
    3.  レスポンス返却
-   DB操作: `INSERT INTO cart_cart (...) VALUES (...)`

PUT /cart/cart

-   説明: カートアイテムを更新
-   Controller: `CartController.updateCartItem(cartItemDto)`
-   Service手順:
    1.  対象カートアイテムの存在確認
    2.  DBで更新
    3.  レスポンス返却
-   DB操作: `UPDATE cart_cart SET ... WHERE id=?`

### 4.2.6 注文サービス

**Order管理**

DELETE /order/order/{id}

-   説明: IDに基づいて注文を削除
-   Controller: `OrderController.deleteOrder(id)`
-   Service手順:
    1.  対象注文の存在確認
    2.  DBから削除
    3.  結果返却
-   DB操作: `DELETE FROM order_order WHERE id=?`

GET /order/order/{id}

-   説明: IDに基づいて注文を取得
-   Controller: `OrderController.getOrderById(id)`
-   Service手順:
    1.  DBから注文情報取得
    2.  レスポンス返却
-   DB操作: `SELECT * FROM order_order WHERE id=?`

GET /order/order/token

-   説明: 決済ページ進入時にトークンを生成して二重注文を防止
-   Controller: `OrderController.generateOrderToken()`
-   Service手順:
    1.  一意のトークン生成
    2.  セッションまたはDBに保存
    3.  トークン返却

GET /order/order/selectAll

-   説明: すべての注文を取得
-   Controller: `OrderController.selectAll()`
-   Service手順:
    1.  DBからすべての注文取得
    2.  レスポンス返却
-   DB操作: `SELECT * FROM order_order`

GET /order/order/poll/{id}

-   説明: 注文状態をポーリングで更新
-   Controller: `OrderController.pollOrderStatus(id)`
-   Service手順:
    1.  DBから注文状態を取得
    2.  状態に応じて更新処理
    3.  最新状態を返却
-   DB操作: `SELECT status FROM order_order WHERE id=?`

POST /order/order

-   説明: 注文を追加
-   Controller: `OrderController.addOrder(orderDto)`
-   Service手順:
    1.  入力バリデーション
    2.  DBにINSERT
    3.  関連在庫・支払い処理呼び出し（必要に応じて）
    4.  レスポンス返却
-   DB操作: `INSERT INTO order_order (...) VALUES (...)`

PUT /order/order

-   説明: 注文を更新
-   Controller: `OrderController.updateOrder(orderDto)`
-   Service手順:
    1.  対象注文の存在確認
    2.  DBで更新
    3.  レスポンス返却
-   DB操作: `UPDATE order_order SET ... WHERE id=?`

**OrderItem管理**

DELETE /order/orderItem/{id}

-   説明: IDに基づいて注文アイテムを削除
-   Controller: `OrderItemController.deleteOrderItem(id)`
-   Service手順:
    1.  対象OrderItemの存在確認
    2.  DBから削除
    3.  結果返却
-   DB操作: `DELETE FROM order_order_item WHERE id=?`

GET /order/orderItem/{id}

-   説明: IDに基づいて注文アイテムを取得
-   Controller: `OrderItemController.getOrderItemById(id)`
-   Service手順:
    1.  DBからOrderItem情報取得
    2.  レスポンス返却
-   DB操作: `SELECT * FROM order_order_item WHERE id=?`

GET /order/orderItem/selectAll/{id}

-   説明: 指定Orderのすべての注文アイテムを取得
-   Controller: `OrderItemController.selectAllByOrderId(orderId)`
-   Service手順:
    1.  DBからOrderItemリスト取得
    2.  レスポンス返却
-   DB操作: `SELECT * FROM order_order_item WHERE order_id=?`

POST /order/orderItem

-   説明: 注文アイテムを追加
-   Controller: `OrderItemController.addOrderItem(orderItemDto)`
-   Service手順:
    1.  入力バリデーション
    2.  DBにINSERT
    3.  レスポンス返却
-   DB操作: `INSERT INTO order_order_item (...) VALUES (...)`

PUT /order/orderItem

-   説明: 注文アイテムを更新
-   Controller: `OrderItemController.updateOrderItem(orderItemDto)`
-   Service手順:
    1.  対象OrderItemの存在確認
    2.  DBで更新
    3.  レスポンス返却
-   DB操作: `UPDATE order_order_item SET ... WHERE id=?`

### 4.2.7 決済サービス

**Pay管理**

GET /pay/pay/search/{id}

-   説明: 指定IDの支払い情報を取得
-   Controller: `PayController.searchPayById(id)`
-   Service手順:
    1.  DBまたは外部決済サービスから支払い情報を取得
    2.  レスポンス返却
-   DB操作: `SELECT * FROM pay_pay WHERE id=?`

GET /pay/pay/create/{id}/{price}

-   説明: 指定IDと金額で支払いを作成
-   Controller: `PayController.createPay(id, price)`
-   Service手順:
    1.  支払い情報のバリデーション
    2.  DBに支払いレコード作成
    3.  外部決済サービスに支払いリクエスト送信
    4.  レスポンス返却（支払いURLやステータスなど）
-   DB操作: `INSERT INTO pay_pay (order_id, amount, status, ...) VALUES (?, ?, ?, ...)`



------

## 4.3 データベース詳細設計

### 4.3.1 ユーザーサービス

```sql
CREATE TABLE `user_user` (
	`id` BIGINT(20) NOT NULL COMMENT '用户ID',
	`name` VARCHAR(50) NULL DEFAULT NULL COMMENT '用户名' COLLATE 'utf8mb4_general_ci',
	`password` VARCHAR(255) NULL DEFAULT NULL COMMENT '密码' COLLATE 'utf8mb4_general_ci',
	`email` VARCHAR(100) NULL DEFAULT NULL COMMENT '邮箱' COLLATE 'utf8mb4_general_ci',
	`phone` VARCHAR(20) NULL DEFAULT NULL COMMENT '手机号' COLLATE 'utf8mb4_general_ci',
	`role` CHAR(50) NULL DEFAULT NULL COMMENT '角色 USER用户 ADMIN管理员' COLLATE 'utf8mb4_general_ci',
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='用户表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

```

###  4.3.2 商品サービス

```sql
CREATE TABLE `product_attr` (
	`id` BIGINT(20) NOT NULL COMMENT '属性ID',
	`name` VARCHAR(100) NULL DEFAULT NULL COMMENT '属性名称' COLLATE 'utf8mb4_general_ci',
	`category_id` BIGINT(20) NULL DEFAULT NULL COMMENT '所属分类ID',
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='商品属性表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

```

```sql
CREATE TABLE `product_attr_value` (
	`id` BIGINT(20) NOT NULL COMMENT '属性值ID',
	`name` VARCHAR(100) NULL DEFAULT NULL COMMENT '属性值名称' COLLATE 'utf8mb4_general_ci',
	`attr_id` BIGINT(20) NULL DEFAULT NULL COMMENT '所属属性ID',
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='商品属性值表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

```



```sql
CREATE TABLE `product_brand` (
	`id` BIGINT(20) NOT NULL COMMENT '品牌ID',
	`name` VARCHAR(100) NULL DEFAULT NULL COMMENT '品牌名称' COLLATE 'utf8mb4_general_ci',
	`picture` VARCHAR(500) NULL DEFAULT NULL COMMENT '品牌图片URL' COLLATE 'utf8mb4_general_ci',
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='商品品牌表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

```

```sql
CREATE TABLE `product_category` (
	`id` BIGINT(20) NOT NULL COMMENT '分类ID',
	`name` VARCHAR(100) NULL DEFAULT NULL COMMENT '分类名称' COLLATE 'utf8mb4_general_ci',
	`parent_id` BIGINT(20) NULL DEFAULT '0' COMMENT '父级分类ID，0表示顶级分类',
	`sort` INT(11) NULL DEFAULT '0' COMMENT '排序字段',
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='商品分类表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

```

```sql
CREATE TABLE `product_product_sku` (
	`id` BIGINT(20) NOT NULL COMMENT 'SKU ID',
	`name` VARCHAR(100) NULL DEFAULT NULL COMMENT 'SKU名称' COLLATE 'utf8mb4_general_ci',
	`spu_id` BIGINT(20) NULL DEFAULT NULL COMMENT '所属SPU ID',
	`picture` VARCHAR(500) NULL DEFAULT NULL COMMENT 'SKU图片' COLLATE 'utf8mb4_general_ci',
	`price` DECIMAL(10,2) NULL DEFAULT NULL COMMENT 'SKU价格',
	`stock` INT(11) NULL DEFAULT '0' COMMENT '库存数量',
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='商品SKU表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

```

```sql
CREATE TABLE `product_product_spu` (
	`id` BIGINT(20) NOT NULL COMMENT 'SPU ID',
	`name` VARCHAR(100) NULL DEFAULT NULL COMMENT '商品名称' COLLATE 'utf8mb4_general_ci',
	`brand_id` BIGINT(20) NULL DEFAULT NULL COMMENT '品牌ID',
	`category1_id` BIGINT(20) NULL DEFAULT NULL COMMENT '一级分类ID，冗余存储，方便查询',
	`category2_id` BIGINT(20) NULL DEFAULT NULL COMMENT '二级分类ID',
	`status` INT(11) NULL DEFAULT NULL COMMENT '状态 0-下架 1-上架',
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='商品SPU表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

```

```sql
CREATE TABLE `product_sku_attr_value` (
	`id` BIGINT(20) NOT NULL COMMENT '主键ID',
	`sku_id` BIGINT(20) NULL DEFAULT NULL COMMENT 'SKU ID',
	`attr_id` BIGINT(20) NULL DEFAULT NULL COMMENT '属性ID',
	`value_id` BIGINT(20) NULL DEFAULT NULL COMMENT '属性值ID',
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='SKU属性值关联表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

```



### 4.3.3 注文サービス

```sql
CREATE TABLE `order_order` (
	`id` BIGINT(20) NOT NULL COMMENT '订单ID',
	`user_id` BIGINT(20) NULL DEFAULT NULL COMMENT '用户ID',
	`total_amount` DECIMAL(10,2) NULL DEFAULT NULL COMMENT '订单总金额',
	`status` INT(11) NULL DEFAULT '0' COMMENT '订单状态 0-待支付 1-已支付 2-已发货 3-已完成 4-已关闭',
	`receiver_name` VARCHAR(100) NULL DEFAULT NULL COMMENT '收货人姓名' COLLATE 'utf8mb4_general_ci',
	`receiver_phone` VARCHAR(20) NULL DEFAULT NULL COMMENT '收货人电话' COLLATE 'utf8mb4_general_ci',
	`receiver_address` VARCHAR(255) NULL DEFAULT NULL COMMENT '收货人地址' COLLATE 'utf8mb4_general_ci',
	`trade_id` VARCHAR(100) NULL DEFAULT NULL COMMENT '交易号' COLLATE 'utf8mb4_general_ci',
	`pay_time` DATETIME NULL DEFAULT NULL COMMENT '支付时间',
	`delivery_time` DATETIME NULL DEFAULT NULL COMMENT '发货时间',
	`receive_time` DATETIME NULL DEFAULT NULL COMMENT '收货时间',
	`close_time` DATETIME NULL DEFAULT NULL COMMENT '关闭时间',
	`remark` VARCHAR(255) NULL DEFAULT NULL COMMENT '订单备注' COLLATE 'utf8mb4_general_ci',
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='订单表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

```

```sql
CREATE TABLE `order_order_item` (
	`id` BIGINT(20) NOT NULL COMMENT '主键ID',
	`order_id` BIGINT(20) NULL DEFAULT NULL COMMENT '订单ID',
	`sku_id` BIGINT(20) NULL DEFAULT NULL COMMENT 'SKU ID',
	`sku_name` VARCHAR(255) NULL DEFAULT NULL COMMENT 'SKU名称' COLLATE 'utf8mb4_general_ci',
	`sku_picture` VARCHAR(500) NULL DEFAULT NULL COMMENT 'SKU图片' COLLATE 'utf8mb4_general_ci',
	`sku_price` DECIMAL(10,2) NULL DEFAULT NULL COMMENT '商品单价',
	`sku_quantity` INT(11) NULL DEFAULT NULL COMMENT '商品数量',
	`total_price` DECIMAL(10,2) NULL DEFAULT NULL COMMENT '总价',
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='订单项表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

```
------


# 5 製造

------

コードする

# 6 単体試験

------

## 6.1 テスト目的

- 各モジュール・クラス・関数が仕様通りに動作することを確認
- 入力値に対する出力値の正確性を検証
- 異常系処理（エラー入力・例外処理）の確認

------

## 6.2 テスト対象

- ユーザー管理モジュール
- 商品管理モジュール（SPU/SKU/属性/ブランド/カテゴリ）
- カート・注文モジュール
- 検索モジュール
- ファイル管理モジュール
- 支払いモジュール

------

## 6.3 テスト票例

| No   | テスト項目      | 入力値                    | 期待結果                       | 実施結果 | コメント |
| ---- | --------------- | ------------------------- | ------------------------------ | -------- | -------- |
| 1    | ユーザー登録    | name, email, password     | 正常に登録される               | ○        | -        |
| 2    | 商品SKU在庫更新 | sku_id=1001, num=5        | 在庫が+5される                 | ○        | -        |
| 3    | 注文作成        | cart_id=2001              | 注文が作成される、在庫が減少   | ○        | -        |
| 4    | 商品検索        | キーワード="スマホ"       | 該当商品のリストが返る         | ○        | -        |
| 5    | 支払い処理      | order_id=3001, price=5000 | 支払い成功、注文ステータス更新 | ○        | -        |

------

## 6.4 実施結果

 単体試験は Postman および JUnit を使用して実施。
 DB はテスト環境（MySQL Docker）で実行し、外部サービス（PayPay API、検索モジュール）は Mock 化して検証した。 

------

# 7 結合試験

------

## 7.1 試験目的

- 画面、API、データベース間の正しい連携を確認
- ユーザー操作から最終結果までの業務フローが正常に動作することを検証
- モジュール間のインターフェース不具合を早期発見

------

## 7.2 試験対象

- ユーザー画面 → ユーザー管理API → DB
- 商品検索画面 → 検索API → DB
- カート・注文画面 → 注文API → SKU在庫更新 → DB
- 支払い画面 → 支払いAPI → 注文ステータス更新 → DB

------

## 7.3 テスト票例

| No   | 試験項目                   | 操作 / 入力               | 期待結果                                         | 実施結果 | コメント |
| ---- | -------------------------- | ------------------------- | ------------------------------------------------ | -------- | -------- |
| 1    | ログイン・ユーザー情報取得 | ユーザーID=1001でログイン | APIで正しいユーザー情報が返る                    | ○        | -        |
| 2    | 商品検索・表示             | キーワード="スマホ"       | 該当商品のリストが画面に表示される               | ○        | -        |
| 3    | カート追加 → 注文作成      | SKU=2001, 数量=2          | 注文作成後在庫が減少、DB反映                     | ○        | -        |
| 4    | 支払い処理                 | order_id=3001, price=5000 | 支払い成功後、注文ステータスが「支払い済」に更新 | ○        | -        |
| 5    | 注文履歴表示               | ユーザーID=1001           | 注文履歴が画面に表示される                       | ○        | -        |

------

## 7.4 実施結果

- 正常系・異常系ともに各モジュール間で仕様通りの連携を確認
- 問題点は開発チームへフィードバック済み
- 結合試験を通過し、総合テストフェーズへ移行可能

------

# 8 総合試験

------

## 8.1 試験目的

- ユーザー視点での業務フローが正しく動作することを確認
- 単体試験・結合試験での問題が解消され、システム全体が安定していることを検証
- エラー時の画面表示や例外処理が仕様通りであることを確認

------

## 8.2 試験対象

- ユーザー登録からログイン、商品検索、カート追加、注文作成、支払いまでの一連フロー
- 管理者画面での商品登録・編集・削除、注文管理フロー
- APIとDB、画面の連携全般

------

## 8.3 テスト票例

| No   | 試験項目               | 操作 / 入力                             | 期待結果                           | 実施結果 | コメント |
| ---- | ---------------------- | --------------------------------------- | ---------------------------------- | -------- | -------- |
| 1    | ユーザー登録・ログイン | 新規ユーザー登録 → ログイン             | ログイン成功、ユーザー情報表示     | ○        | -        |
| 2    | 商品検索・カート追加   | キーワード="イヤホン" → SKU=101, 数量=1 | カートに正しく追加される           | ○        | -        |
| 3    | 注文作成・支払い       | 注文作成 → 支払い完了                   | 注文ステータスが「支払い済」に更新 | ○        | -        |
| 4    | 注文履歴表示           | ユーザーID=1001                         | 注文履歴に作成した注文が表示       | ○        | -        |
| 5    | 管理者商品操作         | 商品登録・編集・削除                    | DBおよび画面に反映される           | ○        | -        |

------

## 8.4 実施結果

- 業務フロー全体で正常に動作を確認
- 異常系テストも仕様通りにエラー処理が機能
- 総合試験を通過し、システムの本番リリース準備が完了

------

# 9 運用試験

------

## 9.1 試験目的

- 運用手順が正しく実施できることを確認
- 障害発生時のエラー対応手順が仕様通りであることを検証
- 本番運用における運用者の操作が安全で正確であることを確認

------

## 9.2 試験対象

- サーバ起動／停止手順
- バックアップ・リストア手順
- 障害時のログ確認・復旧手順
- 通知・アラートの動作確認
- 定期メンテナンス手順

------

## 9.3 テスト票例

| No   | 試験項目         | 操作 / 入力                  | 期待結果                               | 実施結果 | コメント |
| ---- | ---------------- | ---------------------------- | -------------------------------------- | -------- | -------- |
| 1    | サーバ起動手順   | Webサーバ・DBサーバ起動      | サービスが正常に起動し、アクセス可能   | ○        | -        |
| 2    | バックアップ手順 | 定期バックアップ実行         | バックアップファイルが正しく作成       | ○        | -        |
| 3    | 障害対応手順     | DB接続障害発生               | 障害通知が届き、復旧手順で復旧完了     | ○        | -        |
| 4    | 定期メンテナンス | データクリーニング・ログ整理 | 作業完了後もサービス正常稼働           | ○        | -        |
| 5    | エラー対応       | 不正入力や例外発生           | エラーが通知され、運用手順通り対応可能 | ○        | -        |

------

## 9.4 実施結果

- 運用手順を順守し、すべての操作で期待結果を確認
- 想定外障害にも手順通り対応可能であることを検証
- 本番運用準備が完了

------

# 10 環境構築

------

## 10.1 構築目的

- AWS上に開発／テスト環境を構築し、アプリケーションの動作確認を行う
- 総合テスト（エンドツーエンドテスト）を実施する
- インフラ構成の検証と最適化を行う

------

## 10.2 環境構成

- クラウドプロバイダ：AWS
- ネットワーク：VPC, サブネット, セキュリティグループ
- サーバ：
  - EC2（アプリケーションサーバ）
  - RDS（MySQL）
  - S3（ファイルストレージ）
  - ElastiCache（Redis）
- メッセージキュー：RabbitMQ
- その他：CloudWatch（ログ監視）、Route53（DNS）

------

## 10.3 構築手順

1. VPC・サブネット・セキュリティグループを設定
2. EC2インスタンスにOS・必要ソフトウェアをインストール
3. RDSを作成し、DBスキーマを投入
4. S3バケットを作成し、必要なフォルダ構成を準備
5. RabbitMQおよびRedisを構築
6. アプリケーションをデプロイ（Docker / CI/CD）
7. 必要な環境変数・設定ファイルを配置

------

## 10.4 動作確認

- アプリケーションの起動確認
- API疎通確認
- DB接続確認
- ファイルアップロード・ダウンロード確認
- メッセージキュー動作確認
- キャッシュ動作確認

