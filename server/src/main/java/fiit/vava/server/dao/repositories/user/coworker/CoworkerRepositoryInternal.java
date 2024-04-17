package fiit.vava.server.dao.repositories.user.coworker;

import fiit.vava.server.Coworker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CoworkerRepositoryInternal extends CoworkerRepository {

    private final List<Coworker> coworkers = new ArrayList<>();

    @Override
    public Coworker save(Coworker toSave) {
        if (toSave.getId() != null && !toSave.getId().isEmpty()) {
            // Remove the old record if it exists
            Coworker finalToSave = toSave;
            coworkers.removeIf(coworker -> coworker.getId().equals(finalToSave.getId()));
        } else {
            // Assign a new UUID if saving a new coworker
            toSave = Coworker.newBuilder()
                    .setId(UUID.randomUUID().toString())
                    .setUser(toSave.getUser())
                    .setCreatedBy(toSave.getCreatedBy())
                    .addAllPreferredCountries(toSave.getPreferredCountriesList())
                    .build();
        }

        coworkers.add(toSave);
        return toSave;
    }

    @Override
    public List<Coworker> findAll() {
        // Return a new list to avoid modification of the internal list
        return new ArrayList<>(coworkers);
    }

    @Override
    public Coworker findById(String id) {
        return coworkers.stream()
                .filter(coworker -> coworker.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
