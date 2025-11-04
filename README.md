# MyShop - Java バックエンドECシステム

[![Java](https://img.shields.io/badge/Java-8-blue)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7-green)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-2.7-brightgreen)](https://vuejs.org/)
[![Docker](https://img.shields.io/badge/Docker-25-blue)](https://www.docker.com/)
[![AWS](https://img.shields.io/badge/AWS-Cloud-orange)](https://aws.amazon.com/)

---

## プロジェクト概要

MyShopは、ユーザー向けショッピング、管理システム、マイクロサービスを統合したECプラットフォームです。
フロントエンドはVue + Nuxt、バックエンドはSpring Bootマイクロサービスで構築され、AWS上でオンラインデモ可能です。

**オンラインデモ**:  [http://myshop.mhxi.top]

---

## 技術スタック

* **バックエンド**: Java 8, Spring Boot 2.7, Spring Security, MyBatis, Maven, Git, Spring Cloud
* **マイクロサービス**: 9モジュール（ユーザー、商品、注文、カート、Eureka登録センター、Gateway、S3ファイルアップロード、PayPay決済、検索）
* **フロントエンド**: Vue 2 + Nuxt.js, Element UI
* **データベース**: MySQL, Redis
* **CI/CD**: Jenkins, Docker, AWS
* **その他技術**: RabbitMQ, Elasticsearch, AWS S3/EC2, PayPay決済, Swagger API

---

## デモの操作ガイド

1. **管理者ログイン**

   * アカウント: `admin`
   * パスワード: `admin`
   * ログインすると管理画面に移動。
2. **管理画面操作**

   * ブランド、カテゴリ（一级・二级）、属性、属性値の追加・編集・削除
   * 商品の追加: まずSPUを作成し、その後SKUを追加
   * SPU詳細で「上架」に変更すると、そのSPU下の全SKUが検索システムに反映
3. **フロントエンド体験**

   * 左上の「MyShop」をクリックして前台システムへ
   * 検索バーで商品検索、属性値クリックでフィルタリング、ソート機能あり
   * 商品詳細ページで同一SPU内の他SKUに切り替え可能
   * 商品をカートに追加し、注文へ進む
4. **注文・決済**

   * 注文画面で商品を確認後、PayPay（サンドボックスモード）で支払い可能
   * 開発者モード: 上部空白部分を7回クリック → 開発者アカウントでログイン

     * アカウント: `09065882714`
     * パスワード: `FTX0cryVRC`
   * 支払い後、ページは「マイ注文」へ遷移し、詳細を確認可能

---


