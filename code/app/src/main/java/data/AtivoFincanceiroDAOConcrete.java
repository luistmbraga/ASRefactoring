package data;

import business.AtivoFinanceiro;
import business.CFD;
import business.Petroleo;

import java.sql.*;


import java.util.ArrayList;
import java.util.List;

public class AtivoFincanceiroDAOConcrete implements AtivoFinanceiroDAO {
    DBConnection Conn = new SQLConnection();

    @Override
    public AtivoFinanceiro get(String id) {
        
        AtivoFinanceiro a = null;
        try{
            this.Conn.connect();
            ResultSet rs = this.Conn.executeQuery("select * from AtivoFinanceiro where Nome='" + id +"'");
            if(rs.next()){
                a = new AtivoFinanceiro(rs.getString("Nome"),rs.getDouble("ValorUnit"),"") {};
            }
        }
        catch (Exception e ){e.printStackTrace();}
        finally {
            Conn.disconnect();
        }
        return a;
    }



    @Override
    public void delete(String id) {

        this.Conn.connect();

        try {
            this.Conn.executeUpdate("delete from AtivoFinanceiro where Nome='" + id +"'");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Conn.disconnect();
        }

    }

    @Override
    public String put(AtivoFinanceiro obj) {
        try{
            this.Conn.connect();
            ResultSet rs = this.Conn.executeQuery("select * from AtivoFinanceiro where Nome='" + obj.getCompany() +"'");
            if(rs.next()){
                this.Conn.executeUpdate("Update AtivoFinanceiro set ValorUnit=" + obj.getValue()+ ", Type='" + obj.getType()+ "'" +
                        " where Nome ='" + obj.getCompany() +"'");
            }
            else {
                this.Conn.executeUpdate("delete from AtivoFinanceiro where Nome='" + obj.getCompany() + "'");

                String cmd = "insert into AtivoFinanceiro (Nome,ValorUnit,Type) values('" + obj.getCompany() + "'," + obj.getValue() + ",'" + obj.getType() + "')";

                this.Conn.executeUpdate(cmd);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }finally {
            Conn.disconnect();
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
        
        AtivoFinanceiro a = null;
        try{
            this.Conn.connect();
            ResultSet rs = this.Conn.executeQuery("select * from AtivoFinanceiro");
            while(rs.next()){
                a = new AtivoFinanceiro(rs.getString("Nome"),rs.getDouble("ValorUnit"),rs.getString("Type")) {};
                ativos.add(a);
            }

        }
        catch (Exception e ){e.printStackTrace();}
        finally {
            Conn.disconnect();
        }
        return ativos;
    }

    @Override
    public List<CFD> getCFDs(AtivoFinanceiro ativoFinanceiro) {
        List<CFD> cfds = new ArrayList<>();
        
        CFD cfd;
        CFDDAO cfddao = DAOFactory.getFactory().newCFDDAO();
        try{
            this.Conn.connect();
            ResultSet rs = this.Conn.executeQuery("select Id from CFD inner join AtivoFinanceiro on CFD.AtivoFinanceiro_Nome = AtivoFinanceiro.Nome where AtivoFinanceiro.Nome='"+ ativoFinanceiro.getCompany() + "' and Id not in (select Id from CFDVendido);");
            while(rs.next()){
                cfd = cfddao.get(rs.getInt("Id"));
                cfds.add(cfd);
            }
        }
        catch (Exception e ){e.printStackTrace();}
        finally {
            Conn.disconnect();
        }
        return cfds;
    }

    //teste
    public static void main(String[] args) {
        AtivoFincanceiroDAOConcrete a = new AtivoFincanceiroDAOConcrete();

        AtivoFinanceiro af = new Petroleo("Petroleo",11);
        List<AtivoFinanceiro> ativos = new ArrayList<>();

        a.getAll();
    }


}