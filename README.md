<!-- common contents -->

<div style="text-align: center">
    <img width="160" src="logo.svg" alt="logo"><br/>
    projectGDT - for a more connected Minecraft world!<br/>
    QQ Group:
    <a href="https://qm.qq.com/cgi-bin/qm/qr?k=jNFTovEpc0WDFtbSbUMrbQ0NyUgDpnCu&jump_from=webapi&authKey=6oBQQeoeB6gA7+AljJK7AV1IUEjkk/HpkvxrBNgAQtpxPtw230h4GQrp56nTw81I">
        162779544
    </a>
</div>

---

# daemon

projectGDT 的子项目之一，也是最为基础的子项目。

承担了维护玩家与服务器数据、认证、响应 HTTP 请求、与 QQ Bot 通讯，以及分发文件的任务。

## 隐私安全

### 一个承诺

我们承诺：运行在 projectGDT 中心服务器的 Daemon 进程，使用由在 github 等平台**完全开源**的代码编译而成的文件。

### 使用的隐私信息及用途

#### authCode

在 projectGDT 账户绑定 Java 正版账号或 LittleSkin 外置登录账号时，用于验证此账号确实拥有 Minecraft 游戏，并获取玩家代号 (playerName) 和 UUID。

authCode 从浏览器端获取，并通过回调链接**明文**传递给 projectGDT 的 Daemon。

authCode 有效期 1 小时，且是一次性的，无需担心隐私泄露风险。

通过 authCode 获取正版账户信息时，认证服务器同时还会返回 minecraftAccessToken、refreshToken 等更加敏感的信息。我们**不会**保存这些信息，详见代码。