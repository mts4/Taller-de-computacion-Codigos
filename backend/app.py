from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/usuario', methods=['GET'])
def obtener_usuario():
    return jsonify({
        "nombre": "Benjamín Neira",
        "email": "b.neira04@ufromail.cl",
        "rut": "12.345.678-9"
    })

@app.route('/login', methods=['POST'])
def login():
    data = request.get_json() or {}
    usuario = data.get('login')
    password = data.get('password')

    if usuario == "admin" and password == "1234":
        return jsonify({"mensaje": "Autenticación exitosa"})
    else:
        return jsonify({"mensaje": "Credenciales incorrectas"})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
