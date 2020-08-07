# SearchFlights
An Android App requesting flights from Endinburgh to London using the [TravelAPI](https://skyscanner.github.io/slate/) from Skyscanner.
It keeps pooling new results every second until there are no more results to show.

# Stack
Dagger, Rx, Retrofit, Gson
Unit tests: Mockito + Mockito Kotlin, JUnit + JUnitParams

# Arch
This app uses a reactive MVP approach on the presentation layer to listen to changes on a stream 
of events and renders the correct results on the screen.

Clean Architecture is used for communicating data through different layers.

To hold state over configuration changes it uses an `AndroidViewModel` object as an instance retainer.






