alter table user_info
    add `ip` json not null comment '用户的ip信息' after `extend`;
update user_info
set `ip` = '{
  "firstIp": "121.40.134.96",
  "latestIp": "58.48.23.111",
  "firstRegion": "浙江·杭州",
  "latestRegion": "湖北·武汉"
}'
where id = 1;