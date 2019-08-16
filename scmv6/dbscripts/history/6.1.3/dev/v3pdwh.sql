-- 数据库脚本登记示例

--原因:修改查重表的描述信息  2018-9-17 YJ begin

comment on column V_PDWH_DUPLICATE.hash_doi is 'doi生成的hash值';

comment on column V_PUB_PDWH.UPDATE_MARK is '更新标记：1.在线导入，且未修改；2.在线导入，并已修改；3.手工录入（基准库默认1）';

--原因:修改查重表的描述信息  2018-9-17 YJ end