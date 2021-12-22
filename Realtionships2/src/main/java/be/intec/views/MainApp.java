package be.intec.views;

import javax.persistence.EntityManager;

import be.intec.models.Branch;
import be.intec.models.Company;
import be.intec.utils.JPAFactory;

public class MainApp {

    public static void main(String[] args) {

        Branch zele = new Branch();
        zele.setName("MC Donald's Zele");

        Branch centralStation = new Branch();
        centralStation.setName("MC Donald's Central Station");

        Company mcDonalds = new Company();
        mcDonalds.setName("MC Donald's");

        EntityManager em = JPAFactory.getEntityManagerFactory().createEntityManager();

        em.getTransaction().begin();

        em.persist(mcDonalds);

        // zele.setCompany(mcDonalds);
        // centralStation.setCompany(mcDonalds);

        em.persist(zele);
        em.persist(centralStation);

       // mcDonalds.getBranches().add(zele);
        // mcDonalds.getBranches().add(centralStation);

        em.getTransaction().commit();

        em.close();

    }

}
