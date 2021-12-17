package be.intec.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @parametized_type E is het datatype van entiteit.
 * @parametized_type ID is het datatype van de primaire-sleutel
 */
public interface IUserRepository<E, ID> {

    /**
     * @param e Nieuwe entiteit
     * @return Laatste gecreëerde primaire-sleutel.
     */
    ID save(E e);

    /**
     * De methode is default, dus GEEN verplicht om over te schrijven.
     *
     * @param eList Een verzameling van entiteiten.
     * @return Een verzameling van succes gecreëerde primaire-sleutels.
     * Als er 10 elementen zijn toegevoegd maar 4 waren succes,
     * de collectie gaat bevatten enkel 4 ID's.
     */
    default List<ID> saveAll(List<E> eList) {
        final List<ID> savedIdList = new ArrayList<>();

        for (E e : eList) {
            ID nextSavedId = save(e);
            savedIdList.add(Objects.requireNonNull(nextSavedId));
        }

        return savedIdList;
    }

    /**
     * @param id    Primaire-sleutel van het bericht.
     * @param email De email van de gebruiker
     * @return Het laatst bijgewerkt primair-sleutel
     */
    ID updateEmail(ID id, String email);


    /**
     * @param id Primaire-sleutel van de gebruiker.
     * @return Laatst verwijderde PS (Primaire-sleutel).
     */
    ID deleteById(ID id);

    /**
     * @param id Primaire-sleutel van de gebruiker.
     * @return De gebruiker matcht met de PS.
     */
   E findById(ID id);

    /**
     * @return All gebruikers-entiteiten.
     */
    // OPTIONEEL
    List<E> findAll();

    /**
     * @param firstName De voornaam van de gebruiker
     * @param lastName De familienaam van de gebruiker.
     * @param email De email van de gebruiker
     * @return Zoekt en retourneert verzameling van de entiteiten die matchen met voornaam, familienaam, of email.
     */
    List<E> search(String firstName, String lastName, String email);

    // OPTIONEEL
    /**
     * @param email De email van de gebruiker. Moet '@' bevatten.
     * @return Zoekt en retourneert verzameling van de entiteiten die matchen met email.
     *          Als je user-invoer is 'john' moet resultaten teruggeven inclusief 'john@doe.com'
     */
    E findByEmail(String email);

    // OPTIONEEL
    /**
     * @param phone Het telefoonnummer van de gebruiker. Moet minimaal 13 digits bevatten.
     * @return Zoekt en retourneert verzameling van de entiteiten die matchen met email.
     *          Als je user-invoer is 'john' moet resultaten teruggeven inclusief 'john@doe.com'
     */
    E findByPhone(String phone);

}
