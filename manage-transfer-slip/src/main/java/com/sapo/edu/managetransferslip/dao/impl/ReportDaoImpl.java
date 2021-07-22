package com.sapo.edu.managetransferslip.dao.impl;

import com.sapo.edu.managetransferslip.dao.IReportDao;
import com.sapo.edu.managetransferslip.model.dto.statistic.StatisticDateTableDto;
import com.sapo.edu.managetransferslip.model.dto.statistic.StatisticInventoryTableDto;
import com.sapo.edu.managetransferslip.model.dto.statistic.StatisticProductTableDto;
import com.sapo.edu.managetransferslip.model.dto.statistic.StatisticTransferTableDto;
import com.sapo.edu.managetransferslip.repository.InventoriesRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportDaoImpl implements IReportDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate parameterJdbcTemplate;
    private InventoriesRepository inventoriesRepository;

    public ReportDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate parameterJdbcTemplate, InventoriesRepository inventoriesRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.parameterJdbcTemplate = parameterJdbcTemplate;
        this.inventoriesRepository = inventoriesRepository;
    }

    @Override
    public List<StatisticProductTableDto> getSatisticProduct(Date start, Date end, List<Integer> inventory) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        String sql1 = "\n" +
                "select p.id, p.code, p.name, ifnull(sub1.transfer_import, 0) transfer_import, ifnull(product_import,0) product_import,\n" +
                "\tifnull(sub2.transfer_export,0) transfer_export, ifnull(sub2.product_export,0) product_export\n" +
                "from products p\n" +
                "left join( select td.product_id, count(t.id) transfer_import, sum(td.number_pro) product_import\n" +
                "\t\t from transfer t\n" +
                "\t\t join transfer_detail td on t.id = td.transfer_id\n" +
                "\t\t where t.deleted_at is null and t.moving_at is not null\n";

        String sql2 = "\t\t group by td.product_id) sub1 on p.id = sub1.product_id\n" +
                " left join (select td.product_id, count(t.id) transfer_export, sum(td.number_pro) product_export\n" +
                "\t\t from transfer t\n" +
                "\t\t join transfer_detail td on t.id = td.transfer_id\n" +
                "\t\t where t.deleted_at is null and t.finish_at is not null\n";

        String sql3 = "\t\t group by td.product_id) sub2 on p.id = sub2.product_id\n" ;
        String sql ="";

        end = (end == null) ? new Date(System.currentTimeMillis()) : end;
        List<StatisticProductTableDto> list = new ArrayList<>();
        if(start != null){
            sql1 = sql1 + " and t.moving_at BETWEEN :start AND :end ";
            sql2 = sql2 + " and t.finish_at BETWEEN :start AND :end ";

            parameters.addValue("start", start);
            parameters.addValue("end", end);
        }
        if(inventory.size()>0){
            List<String> listInventory = new ArrayList<>();
            inventory.stream().filter(item -> inventoriesRepository.existsById(item)).collect(Collectors.toList());
            inventory.forEach(item -> listInventory.add(item.toString()));
            sql1 = sql1 + " and t.inventory_input_id in ( :identity ) ";
            sql2 = sql2 + " and t.inventory_output_id in ( :identity ) ";
            parameters.addValue("identity",  listInventory);

        }

        sql = sql1 + sql2 + sql3;
        list = parameterJdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper<>(StatisticProductTableDto.class) );

        return list;
    }

    @Override
    public List<StatisticTransferTableDto> getStatisticTranser(Date start, Date end) {
        String sql1 = "select t.id, t.code, t.create_at 'date',u.username, ii.name inventoryInput, iop.name inventoryOutput" +
                ", sub.number_pro product_number, sub.total price from transfer t " +
                "join(select td.transfer_id, count(td.product_id) number_pro, sum(td.number_pro*p.price) total " +
                "from transfer_detail td " +
                "join transfer t on t.id = td.transfer_id " +
                "join products p on td.product_id = p.id ";
        String sql2 = "group by td.transfer_id) sub on t.id = sub.transfer_id " +
                "join inventories ii on t.inventory_input_id = ii.id " +
                "join inventories iop on t.inventory_output_id = iop.id " +
                "join users u on t.user_id = u.id";
        String sql;
        end = (end == null) ? new Date(System.currentTimeMillis()) : end;
        List<StatisticTransferTableDto> list = new ArrayList<>();
        if (start == null) {
            sql = sql1 + sql2;
            list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(StatisticTransferTableDto.class));
        } else {
            sql = sql1 + " where t.create_at >= ? and t.create_at <= ?" + sql2;
            list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(StatisticTransferTableDto.class)
                    , start, end);
        }
        System.out.println(sql);
        return list;


    }

    @Override
    public List<StatisticDateTableDto> getStatisticDate(Date start, Date end, List<Integer> inventory, int type) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String sql = "";

        String sql1 = "select ifnull(sub.moving_at, sub.finish_at) create_date, ifnull(sub.transfer_import, 0) transfer_import, ifnull(sub.product_import, 0) product_import,\n" +
                "\t\tifnull(sub.pro_total_import, 0) pro_total_import, ifnull(transfer_export, 0) transfer_export, ifnull(product_export, 0) product_export,\n" +
                "        ifnull(sub.pro_total_export,0) pro_total_export\n" +
                "from\n" +
                "\t(select * from\n" +
                "\t\t(select Date(t.moving_at) moving_at, count(t.id) transfer_import, ifnull(sum(sub1.product),0) product_import, ifnull(sum(sub1.pro_total),0) pro_total_import\n" +
                "\t\tfrom transfer t\n" +
                "\t\tleft join (select td.transfer_id, count(td.product_id) product, sum(td.number_pro) pro_total\n" +
                "\t\t\t\tfrom transfer_detail td\n" +
                "\t\t\t\tgroup by td.transfer_id) sub1 on t.id = sub1.transfer_id\n" +
                "\t\twhere t.deleted_at is null and Date(t.moving_at) is not null ";

        String sql2 = "group by year(moving_at), month(moving_at), dayofmonth(moving_at)) suba\n" +
                "\tleft join\n" +
                "\t\t(select Date(t.finish_at) finish_at, count(t.id) transfer_export, ifnull(sum(sub1.product),0) product_export, ifnull(sum(sub1.pro_total),0) pro_total_export\n" +
                "\t\tfrom transfer t\n" +
                "\t\tleft join (select td.transfer_id, count(td.product_id) product, sum(td.number_pro) pro_total\n" +
                "\t\t\t\tfrom transfer_detail td\n" +
                "\t\t\t\tgroup by td.transfer_id) sub1 on t.id = sub1.transfer_id\n" +
                "\t\twhere t.deleted_at is null and t.finish_at is not null ";
        String sql3 = "group by year(finish_at), month(finish_at), dayofmonth(finish_at)) subb on suba.moving_at = subb.finish_at\n" +
                "\tunion\n" +
                "\tselect * from\n" +
                "\t\t(select Date(t.moving_at) moving_at, count(t.id) transfer_import, ifnull(sum(sub1.product),0) product_import, ifnull(sum(sub1.pro_total),0) pro_total_import\n" +
                "\t\tfrom transfer t\n" +
                "\t\tleft join (select td.transfer_id, count(td.product_id) product, sum(td.number_pro) pro_total\n" +
                "\t\t\t\tfrom transfer_detail td\n" +
                "\t\t\t\tgroup by td.transfer_id) sub1 on t.id = sub1.transfer_id\n" +
                "\t\twhere t.deleted_at is null and t.moving_at is not null ";

        String sql4 = "group by year(moving_at), month(moving_at), dayofmonth(moving_at)) suba\n" +
                "\tright join\n" +
                "\t\t(select Date(t.finish_at) finish_at, count(t.id) transfer_export, ifnull(sum(sub1.product),0) product_export, ifnull(sum(sub1.pro_total),0) pro_total_export\n" +
                "\t\tfrom transfer t\n" +
                "\t\tleft join (select td.transfer_id, count(td.product_id) product, sum(td.number_pro) pro_total\n" +
                "\t\t\t\tfrom transfer_detail td\n" +
                "\t\t\t\tgroup by td.transfer_id) sub1 on t.id = sub1.transfer_id\n" +
                "\t\twhere t.deleted_at is null and t.finish_at is not null ";

        String sql5 = "group by  year(finish_at), month(finish_at), dayofmonth(finish_at)) subb on suba.moving_at = subb.finish_at) sub\n" +
                "order by create_date desc ";

        end = (end == null) ? new Date(System.currentTimeMillis()) : end;
        if (start != null) {
            sql1 = sql1 + " and t.moving_at BETWEEN :start AND :end ";
            sql2 = sql2 + " and t.finish_at BETWEEN :start AND :end ";
            sql3 = sql3 + " and t.moving_at BETWEEN :start AND :end ";
            sql4 = sql4 + " and t.finish_at BETWEEN :start AND :end ";

            parameters.addValue("start", start);
            parameters.addValue("end", end);
        }
        if (inventory.size() > 0 ) {
            List<String> listInventory = new ArrayList<>();
            inventory.stream().filter(item -> inventoriesRepository.existsById(item)).collect(Collectors.toList());
            inventory.forEach(item -> listInventory.add(item.toString()));
            sql1 = sql1 + " and ( t.inventory_input_id in ( :inventory )) ";
            sql2 = sql2 + " and (t.inventory_output_id in ( :inventory )) ";
            sql3 = sql3 + " and ( t.inventory_input_id in ( :inventory )) ";
            sql4 = sql4 + " and (t.inventory_output_id in ( :inventory )) ";

            parameters.addValue("inventory",  listInventory);
        }
        if (type > 0) {
            switch (type) {
                case 1: {
                    sql1 = sql1 + " group by (t.moving_at)) ";
                    sql2 = sql2 + " group by (t.finish_at)) ";
                    sql3 = sql3 + " group by (t.moving_at)) ";
                    sql4 = sql4 + " group by (t.finish_at)) ";
                    break;
                }
                case 2: {
                    sql1 = sql1 + " group by month(t.moving_at), YEAR(moving_at) ) ";
                    sql2 = sql2 + " group by month(t.finish_at), YEAR(finish_at) ) ";
                    sql3 = sql3 + " group by month(t.moving_at), YEAR(moving_at) ) ";
                    sql4 = sql4 + " group by month(t.finish_at), YEAR(finish_at) ) ";
                    break;
                }
                case 3: {
                    sql1 = sql1 + " group by  YEAR(moving_at) ) ";
                    sql2 = sql2 + " group by  YEAR(finish_at) ) ";
                    sql3 = sql3 + " group by  YEAR(moving_at) ) ";
                    sql4 = sql4 + " group by  YEAR(finish_at) ) ";
                    break;
                }
            }
        }
        sql = sql1 + sql2 + sql3 + sql4 + sql5;
        List<StatisticDateTableDto> list = parameterJdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper<>(StatisticDateTableDto.class));

        return list;
    }

    @Override
    public List<StatisticInventoryTableDto> getSatisticInventory(Date date, List<Integer> inventory, int user) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        String sql1 = "select ifnull(sub.create_input, sub.create_ouput) date_create,i.id inventory_id, i.name inventory, ifnull(sub.transfer_num_input,0) transfer_import\n" +
                "\t\t, ifnull(sub.transfer_num_ouput,0) transfer_export, ifnull(product_input,0) product_import, ifnull(product_output,0) product_export, ifnull(pro_number_input, 0) pro_total_import\n" +
                "\t\t, ifnull(pro_number_output,0) pro_total_export\n" +
                "from(\n" +
                "\tselect *\n" +
                "\tfrom(\n" +
                "\t\tselect t.moving_at create_input,t.inventory_input_id,ifnull( count(t.id),0) transfer_num_input, ifnull(sum(sub.product), 0) product_input\n" +
                "        , ifnull(sum(sub.pro_number), 0) pro_number_input, ifnull(sum(sub.pro_total), 0) pro_total_input\n" +
                "\t\tfrom transfer t\n" +
                "\t\tleft join (\n" +
                "\t\t\tselect td.transfer_id, ifnull(count(p.id),0) product, ifnull(sum(td.number_pro),0) pro_number, ifnull(sum(td.number_pro * p.price),0) pro_total\n" +
                "\t\t\tfrom transfer_detail td\n" +
                "\t\t\tjoin products p on td.product_id = p.id\n" +
                "\t\t\tgroup by td.transfer_id) sub on t.id = sub.transfer_id\n" +
                "\t\twhere t.deleted_at is null and t.moving_at  ";
        String sql2 = " group by year(create_input), month(create_input), dayofmonth(create_input),t.inventory_input_id) sub1\n" +
                "\tleft join(\n" +
                "\t\tselect t.finish_at create_ouput,t.inventory_output_id,ifnull( count(t.id),0) transfer_num_ouput, ifnull(sum(sub.product), 0) product_output\n" +
                "\t\t\t, ifnull(sum(sub.pro_number), 0) pro_number_output, ifnull(sum(sub.pro_total), 0) pro_total_output\n" +
                "\t\tfrom transfer t\n" +
                "\t\tleft join (\n" +
                "\t\t\tselect td.transfer_id, ifnull(count(p.id),0) product, ifnull(sum(td.number_pro),0) pro_number, ifnull(sum(td.number_pro * p.price),0) pro_total\n" +
                "\t\t\tfrom transfer_detail td\n" +
                "\t\t\tjoin products p on td.product_id = p.id\n" +
                "\t\t\tgroup by td.transfer_id) sub on t.id = sub.transfer_id\n" +
                "\t\twhere t.deleted_at is null and t.finish_at is not null  ";
        String sql3 = " group by year(finish_at), month(finish_at), dayofmonth(finish_at),t.inventory_output_id)sub2 on sub1.create_input = sub2.create_ouput and sub1.inventory_input_id = sub2.inventory_output_id\n" +
                "\tunion\n" +
                "\tselect *\n" +
                "\tfrom(\n" +
                "\t\tselect t.moving_at create_input,t.inventory_input_id,ifnull( count(t.id),0) transfer_num_input, ifnull(sum(sub.product), 0) product_input\n" +
                "        , ifnull(sum(sub.pro_number), 0) pro_number_input, ifnull(sum(sub.pro_total), 0) pro_total_input\n" +
                "\t\tfrom transfer t\n" +
                "\t\tleft join (\n" +
                "\t\t\tselect td.transfer_id, ifnull(count(p.id),0) product, ifnull(sum(td.number_pro),0) pro_number, ifnull(sum(td.number_pro * p.price),0) pro_total\n" +
                "\t\t\tfrom transfer_detail td\n" +
                "\t\t\tjoin products p on td.product_id = p.id\n" +
                "\t\t\tgroup by td.transfer_id) sub on t.id = sub.transfer_id\n" +
                "\t\twhere t.deleted_at is null and t.moving_at  ";
        String sql4 = " group by year(create_input), month(create_input), dayofmonth(create_input),t.inventory_input_id) sub1\n" +
                "\tright join(\n" +
                "\t\tselect t.finish_at create_ouput,t.inventory_output_id,ifnull( count(t.id),0) transfer_num_ouput, ifnull(sum(sub.product), 0) product_output\n" +
                "\t\t\t, ifnull(sum(sub.pro_number), 0) pro_number_output, ifnull(sum(sub.pro_total), 0) pro_total_output\n" +
                "\t\tfrom transfer t\n" +
                "\t\tleft join (\n" +
                "\t\t\tselect td.transfer_id, ifnull(count(p.id),0) product, ifnull(sum(td.number_pro),0) pro_number, ifnull(sum(td.number_pro * p.price),0) pro_total\n" +
                "\t\t\tfrom transfer_detail td\n" +
                "\t\t\tjoin products p on td.product_id = p.id\n" +
                "\t\t\tgroup by td.transfer_id) sub on t.id = sub.transfer_id\n" +
                "\t\twhere t.deleted_at is null and t.finish_at is not null ";
        String sql5 = " group by year(finish_at), month(finish_at), dayofmonth(finish_at),t.inventory_output_id)sub2 on sub1.create_input = sub2.create_ouput and sub1.inventory_input_id = sub2.inventory_output_id) sub\n" +
                "join inventories i on sub.inventory_input_id = i.id or sub.inventory_output_id = i.id ";

        if (inventory != null) {
            if(inventory.size() > 0) {
                List<String> listInventory = new ArrayList<>();
                inventory.stream().filter(item -> inventoriesRepository.existsById(item)).collect(Collectors.toList());
                inventory.forEach(item -> listInventory.add(item.toString()));
                sql1 = sql1 + " and ( t.inventory_input_id in ( :inventory )) ";
                sql2 = sql2 + " and (t.inventory_output_id in ( :inventory )) ";
                sql3 = sql3 + " and ( t.inventory_input_id in ( :inventory )) ";
                sql4 = sql4 + " and (t.inventory_output_id in ( :inventory )) ";

                parameters.addValue("inventory", listInventory);
            }
        }
        if (user > 0) {
            sql1 = sql1 + " and t.user_id = :user ";
            sql2 = sql2 + " and t.user_id = :user ";
            sql3 = sql3 + " and t.user_id = :user ";
            sql4 = sql4 + " and t.user_id = :user ";
            parameters.addValue("user", user);
        }
        if (date != null) {
            sql5 = sql5 + " where Date(sub.create_input) = :date or Date(sub.create_ouput) = :date ";
            parameters.addValue("date", date);
        }
        String sql = sql1 + sql2 + sql3 + sql4 + sql5;
        List<StatisticInventoryTableDto> list = parameterJdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper<>(StatisticInventoryTableDto.class));
        return list;
    }
}
