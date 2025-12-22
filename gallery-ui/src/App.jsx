import { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';

// Configure standard API base URL
const API_URL = "http://localhost:8080/api/images";

function App() {
    const [file, setFile] = useState(null);
    const [images, setImages] = useState([]);
    const [uploading, setUploading] = useState(false);

    // Fetch images on component mount
    useEffect(() => {
        fetchImages();
    }, []);

    const fetchImages = async () => {
        try {
            const response = await axios.get(API_URL);
            setImages(response.data);
        } catch (error) {
            console.error("Error fetching images:", error);
        }
    };

    // File Selection
    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };

    // Form Submission
    const handleUpload = async (event) => {
        event.preventDefault();
        if (!file) return;

        if (file.size > 2 * 1024 * 1024) { // 2MB
            alert("File is too large! Please upload an image smaller than 2MB.");
            return; // Stop
        }

        setUploading(true);
        const formData = new FormData();
        formData.append("file", file);

        try {
            await axios.post(`${API_URL}/upload`, formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            });
            await fetchImages();
            setFile(null);
            event.target.reset();
        }
        catch (error) {
            console.error("Error uploading file:", error);

            // NEW: Check if the server sent a specific message
            if (error.response && error.response.data && error.response.data.message) {
                alert(`Error: ${error.response.data.message}`);
            } else {
                alert("Upload failed! Server might be down.");
            }
        }
        finally {
            setUploading(false);
        }
    };

    return (
        <div className="app-container">
            <h1>My Photo Gallery</h1>

            {/* Upload Section */}
            <div className="upload-section">
                <form onSubmit={handleUpload}>
                    <input type="file" onChange={handleFileChange} accept="image/*" />
                    <button type="submit" disabled={!file || uploading}>
                        {uploading ? "Uploading..." : "Upload"}
                    </button>
                </form>
            </div>

            {/* Gallery Grid */}
            <div className="image-grid">
                {images.map((img) => (
                    <div key={img.id} className="image-card">
                        <img src={img.url} alt={img.fileName} />
                        <p>{new Date(img.createdAt).toLocaleDateString()}</p>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default App;