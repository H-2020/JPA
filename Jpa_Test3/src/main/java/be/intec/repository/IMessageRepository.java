package be.intec.repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @parametized_type E is het datatype van entiteit.
 * @parametized_type ID is het datatype van de primaire-sleutel
 * */
public interface IMessageRepository<E, ID> {

    /**
     * @param e Nieuwe entiteit
     * @return Laatste gecreëerde primaire-sleutel.
     */
    ID save(E e);

    /**
     * De methode is default, dus GEEN verplicht om over te schrijven.
     * @param eList Een verzameling van entiteiten.
     * @return Een verzameling van succes gecreëerde primaire-sleutels.
     *          Als er 10 elementen zijn toegevoegd maar 4 waren succes,
     *          de collectie gaat bevatten enkel 4 ID's.
     */
    default List<ID> saveAll(List<E> eList){
        final List<ID> savedIdList = new ArrayList<>();

        for (E e : eList) {
            ID nextSavedId = save(e);
            savedIdList.add(nextSavedId);
        }

        return savedIdList;
    }

    /**
     * @param id Primaire-sleutel van het bericht.
     * @param content De titel van het bericht.
     * @return Het laatst bijgewerkt primair-sleutel
     */
    ID updateSubject(ID id, String content);

    /**
     * @param id Primaire-sleutel van het bericht.
     * @param content De body van het bericht.
     * @return Het laatst bijgewerkt primair-sleutel
     */
    ID updateContent(ID id, String content);

    /**
     * @param id Primaire-sleutel van het bericht.
     * @return Het laatst verwijderde primair-sleutel
     */
    ID deleteById(ID id);


    /**
     * @param id Primaire-sleutel van het bericht.
     * @return Het bericht matcht met PS (Primaire-Sleutel).
     */
    E findById(ID id);

    /**
     * @param limit Hoeveelheid van de entiteiten dia gaat retourneert worden.
     * @param offset Pagina's nummer.
     * @return Een verzameling van berichten.
     * Bijvoorbeeld als de user-invoer limit met 10 offset met 2 is,
     * dan geeft de methode terug 10 resultaten van de 2de pagina.
     */
    // OPTIONEEL
    List<E> findAll(int limit, int offset);

    List<E> findAllBySender(ID fromUserId);

    List<E> findAllFromReceiver(ID toUserId);

}
