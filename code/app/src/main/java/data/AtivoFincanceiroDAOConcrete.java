package data;

import business.AtivoFinanceiro;
import business.CFD;

import java.sql.*;


import java.util.ArrayList;
import java.util.List;

public class AtivoFincanceiroDAOConcrete implements AtivoFinanceiroDAO {
    DBConnection conn = new SQLConnection();

    @Override
    public AtivoFinanceiro get(String id) {
        
        AtivoFinanceiro a = null;
        try{
            this.conn.connect();
            ResultSet rs = this.conn.executeQuery("select * from AtivoFinanceiro where Nome='" + id +"'");
            if(rs.next()){
                a = new AtivoFinanceiro(rs.getString("Nome"),rs.getDouble("ValorUnit"),"") {};
            }
        }
        catch (Exception e ){e.printStackTrace();}
        finally {
            conn.disconnect();
        }
        return a;
    }



    @Override
    public void delete(String id) {

        this.conn.connect();

        try {
            this.conn.executeUpdate("delete from AtivoFinanceiro where Nome='" + id +"'");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            conn.disconnect();
        }

    }

    @Override
    public String put(AtivoFinanceiro obj) {
        try{
            this.conn.connect();
            ResultSet rs = this.conn.executeQuery("select * from AtivoFinanceiro where Nome='" + obj.getCompany() +"'");
            if(rs.next()){
                this.conn.executeUpdate("Update AtivoFinanceiro set ValorUnit=" + obj.getValue()+ ", Type='" + obj.getType()+ "'" +
                        " where Nome ='" + obj.getCompany() +"'");
            }
            else {
                this.conn.executeUpdate("delete from AtivoFinanceiro where Nome='" + obj.getCompany() + "'");

                String cmd = "insert into AtivoFinanceiro (Nome,ValorUnit,Type) values('" + obj.getCompany() + "'," + obj.getValue() + ",'" + obj.getType() + "')";

                this.conn.executeUpdate(cmd);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }finally {
            conn.disconnect();
        }
        return obj.getCompany();
    }



    @Override
    public void replace(String id, AtivoFinanceiro obj) {
        obj.setCompany(id);
        put(obj);
    }

    @Override
    public List<AtivoFinanceiro> getAll(){
        List<AtivoFinanceiro> ativos = new ArrayList<>();
        
        AtivoFinanceiro a ;
        try{
            this.conn.connect();
            ResultSet rs = this.conn.executeQuery("select * from AtivoFinanceiro");
            while(rs.next()){
                a = new AtivoFinanceiro(rs.getString("Nome"),rs.getDouble("ValorUnit"),rs.getString("Type")) {};
                ativos.add(a);
            }

        }
        catch (Exception e ){e.printStackTrace();}
        finally {
            conn.disconnect();
        }
        return ativos;
    }

    @Override
    public List<CFD> getCFDs(AtivoFinanceiro ativoFinanceiro) {
        List<CFD> cfds = new ArrayList<>();
        
        CFD cfd;
        CFDDAO cfddao = DAOFactory.getFactory().newCFDDAO();
        try{
            this.conn.connect();
            ResultSet rs = this.conn.executeQuery("select Id from CFD inner join AtivoFinanceiro on CFD.AtivoFinanceiro_Nome = AtivoFinanceiro.Nome where AtivoFinanceiro.Nome='"+ ativoFinanceiro.getCompany() + "' and Id not in (select Id from CFDVendido);");
            while(rs.next()){
                cfd = cfddao.get(rs.getInt("Id"));
                cfds.add(cfd);
            }
        }
        catch (Exception e ){e.printStackTrace();}
        finally {
            conn.disconnect();
        }
        return cfds;
    }


}