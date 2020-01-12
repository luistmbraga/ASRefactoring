package data;

import business.AtivoFinanceiro;
import business.CFD;
import business.CFDVendido;
import business.Utilizador;

import java.sql.*;

import java.util.ArrayList;

import java.util.List;

public class CFDDAOConcrete implements CFDDAO {
    DBConnection Conn = new SQLConnection();


    @Override
    public double sell(CFDVendido cfd) {
        double value=0;

        this.Conn.connect();

        try {
            this.Conn.executeUpdate("insert into cfdvendido (Id, DataVenda, ValorVenda) values ("+ cfd.getId() + ", NOW() ," + cfd.getSoldValue() + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Conn.disconnect();
        }

        return value;
    }

    @Override
    public double getValue(int id) {
        return 0;
    } //decidimos não fazer pois ja está na classe

    @Override
    public List<CFD> get(Utilizador user) {
        List<CFD> CFDs= new ArrayList<>();
        
        CFD cfd = null;
        UtilizadorDAOConcrete uDAO = new UtilizadorDAOConcrete();
        Utilizador u;
        AtivoFincanceiroDAOConcrete afDAO = new AtivoFincanceiroDAOConcrete();
        AtivoFinanceiro a;
        try{
            this.Conn.connect();
            ResultSet rs = this.Conn.executeQuery("select * from CFD inner join Utilizador on Utilizador.Nome=CFD.Utilizador_Nome where Utilizador.Nome='"+user.getUsername()+"' and not exists (select * from CFDVendido)");
            while (rs.next()) {
                u = uDAO.get(rs.getString("Utilizador_Nome"));
                a = afDAO.get(rs.getString("AtivoFinanceiro_Nome"));
                cfd = new CFD(rs.getDouble("ValorCompra"), rs.getDouble("Unidades") ,rs.getDouble("TopProfit"),
                        rs.getDouble("StopLoss"),rs.getInt("Id"),u,a,
                        rs.getTimestamp("DataVenda").toLocalDateTime());
                CFDs.add(cfd);
            }

        }
        catch (Exception e){e.printStackTrace();}
        finally {
            Conn.disconnect();
        }
        return CFDs;
    }

    @Override
    public Integer put(CFD obj) {
        
        int i = 0;

        this.Conn.connect();

        try {
            this.Conn.executeUpdate("delete from CFD where Id=" + obj.getId());

        String cmd = "insert into CFD (Id,ValorCompra,Unidades,TopProfit,StopLoss,Utilizador_Nome,AtivoFinanceiro_Nome) values ("+
                obj.getId()+ "," +obj.getBoughtValue()+","+obj.getUnits()+","+obj.getTopProfit()+","+obj.getStopLoss()+",'"+obj.getUtilizador().getUsername()+"','"+ obj.getAtivoFinanceiro().getCompany() + "')";

        i = this.Conn.executeUpdate(cmd);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Conn.disconnect();
        }

        return i;
    }

    @Override
    public CFD get(Integer id) {
        
        CFD cfd = null;
        UtilizadorDAOConcrete uDAO = new UtilizadorDAOConcrete();
        Utilizador u;
        AtivoFincanceiroDAOConcrete afDAO = new AtivoFincanceiroDAOConcrete();
        AtivoFinanceiro a;
        try{
            this.Conn.connect();
            ResultSet rs = this.Conn.executeQuery("select * from CFD where Id='"+id+"'");
            if (rs.next()) {
                u = uDAO.get(rs.getString("Utilizador_Nome"));
                a = afDAO.get(rs.getString("AtivoFinanceiro_Nome"));
                cfd = new CFD(rs.getDouble("ValorCompra"), rs.getDouble("Unidades"),
                        rs.getDouble("TopProfit"),rs.getDouble("StopLoss"),
                        rs.getInt("Id"),u,a,
                        rs.getTimestamp("DataCompra").toLocalDateTime());
            }

        }  catch (Exception e){e.printStackTrace();}
        finally {
            Conn.disconnect();
        }

        return cfd;
    }

    public List<CFDVendido> getVendidos(Utilizador u) {
        
        List<CFDVendido> portfolioList = new ArrayList<>();
        CFD cfd;
        AtivoFincanceiroDAOConcrete afDAO = new AtivoFincanceiroDAOConcrete();
        AtivoFinanceiro a;
        try{
            this.Conn.connect();
            ResultSet rs = this.Conn.executeQuery("select * from CFDVendido inner join CFD on CFD.Id=CFDVendido.Id inner join Utilizador on Utilizador.Nome=CFD.Utilizador_Nome where Utilizador.Nome='"+u.getUsername()+"'");
            while (rs.next()) {
                a = afDAO.get(rs.getString("AtivoFinanceiro_Nome"));
                cfd = new CFD(rs.getDouble("ValorCompra"), rs.getDouble("Unidades"),
                        rs.getDouble("TopProfit"), rs.getDouble("StopLoss"),
                        rs.getInt("Id"), u, a, rs.getTimestamp("DataCompra").toLocalDateTime());
                CFDVendido cfdVendido = new CFDVendido(cfd,  rs.getTimestamp("DataVenda").toLocalDateTime() ,
                        rs.getDouble("ValorVenda"));
                portfolioList.add(cfdVendido);
            }
        }
        catch (Exception e){e.printStackTrace();}
        finally {
            Conn.disconnect();
        }

        return portfolioList;
    }

    public List<CFD> getPortfolio(Utilizador u) {
        
        List<CFD> portfolioList = new ArrayList<>();
        CFD cfd;
        AtivoFincanceiroDAOConcrete afDAO = new AtivoFincanceiroDAOConcrete();
        AtivoFinanceiro a;
        try{
            this.Conn.connect();
            ResultSet rs = this.Conn.executeQuery("select * from CFD inner join Utilizador on Utilizador.Nome = CFD.Utilizador_Nome where Utilizador.Nome='"+u.getUsername()+"' and Id not in (select Id from CFDVendido);");
            while (rs.next()) {
                a = afDAO.get(rs.getString("AtivoFinanceiro_Nome"));
                cfd = new CFD(rs.getDouble("ValorCompra"), rs.getDouble("Unidades"),
                        rs.getDouble("TopProfit"), rs.getDouble("StopLoss"),
                        rs.getInt("Id"), u, a,rs.getTimestamp("DataCompra").toLocalDateTime());
                portfolioList.add(cfd);
            }
        }
        catch (Exception e){e.printStackTrace();}
        finally {
            Conn.disconnect();
        }

        return portfolioList;
    }

    @Override
    public void delete(Integer id) {

        this.Conn.connect();

        try {
            this.Conn.executeUpdate("Insert into CFDVendido (Id) values(" + id + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Conn.disconnect();
        }
    }

    @Override
    public void replace(Integer id, CFD obj) {
        obj.setId(id);
        put(obj);
    }

}