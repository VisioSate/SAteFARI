1_ Placer les fichiers .service dans le dossier : /etc/systemd/system/

2_ Installer les services et les activer : 
# chmod 664 /etc/systemd/system/acces_point.service
# chmod 664 /etc/systemd/system/roslaunch.service
# chmod 664 /etc/systemd/system/web_server.service
# systemctl daemon-reload
# systemctl enable acces_point.service
# systemctl enable roslaunch.service
# systemctl enable web_server.service

3_ Placer les fichiers .sh dans le dossier : /usr/local/bin/

4_ Rendre le script exécutable :
# chmod 744 /usr/local/bin/acces_point.sh
# chmod 744 /usr/local/bin/convert.sh
# chmod 744 /usr/local/bin/web_server.sh

5_ Redémarrer la Raspberry et les commandes écrites dans les fichiers .sh s'exécuteront





