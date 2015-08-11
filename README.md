# openalto-client
An open source ALTO client-side library.

# Usage

See [](client/src/main/java/org/openalto/alto/client/Main.java) for examples.

The examples are using a local [palto server][palto-server], modify them to use
a real ALTO server.

[palto-server]: https://github.com/emiapwil/alto-server

# Features Missing:

- How to parse the 'default-alto-network-map'
- How to embed better into Jersey framework
- Introduce the concept of 'DecodingContext" to handle things like
  'default-alto-network-map'
- Use jackson to parse simple JSON objects such as CostType
- The parsing of errors
- The validation of strings such as *Resource ID*, *Tag*, etc.
- A high-level wrapper
