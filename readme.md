![title image](https://cdn.discordapp.com/attachments/851371485313892362/851856324873814026/out.gif)

A completely opensource cape service implementation.

discord: [discord.gg/ZTVmndSScx](https://discord.gg/ZTVmndSScx)  
website: [capes.yourfriend.lv](https://capes.yourfriend.lv)


## Apache
I use apache for the backend.  Here are my vhosts for apache:
```
<VirtualHost *:80>
        ServerName yourfriend.lv
        DocumentRoot "/home/arch/website"

        <Location "/capes">
                ProxyPass "http://localhost:20012/capes"
                ProxyPassReverse "http://localhost:20012/capes"
        </Location>
</VirtualHost>

<VirtualHost *:80>
        DocumentRoot /home/arch/AnarchyCapes/website
        ServerName capes.yourfriend.lv

        <Directory /home/arch/AnarchyCapes/website>
                Options Indexes FollowSymLinks
                AllowOverride None
                Require all granted
                Allow from all
        </Directory>
</VirtualHost>
```